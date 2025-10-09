package io.github.alexshamrai;

import io.github.alexshamrai.config.ConfigReader;
import io.github.alexshamrai.ctrf.model.Test;
import io.github.alexshamrai.model.TestDetails;
import io.github.alexshamrai.util.SummaryUtil;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static io.github.alexshamrai.ctrf.model.Test.TestStatus.FAILED;
import static io.github.alexshamrai.ctrf.model.Test.TestStatus.PASSED;
import static io.github.alexshamrai.ctrf.model.Test.TestStatus.SKIPPED;

public final class CtrfReportManager {

    private static final CtrfReportManager INSTANCE = new CtrfReportManager();

    private final List<Test> tests = new CopyOnWriteArrayList<>();
    private final ConcurrentHashMap<String, TestDetails> testDetailsMap = new ConcurrentHashMap<>();
    private long testRunStartTime;
    private final AtomicBoolean isTestRunStarted = new AtomicBoolean(false);

    private final CtrfReportFileService ctrfReportFileService;
    private final TestProcessor testProcessor;
    private final SuiteExecutionErrorHandler suiteExecutionErrorHandler;
    private final CtrfJsonComposer ctrfJsonComposer;

    private CtrfReportManager() {
        var configReader = new ConfigReader();
        this.ctrfReportFileService = new CtrfReportFileService(configReader);
        this.testProcessor = new TestProcessor(configReader);
        this.suiteExecutionErrorHandler = new SuiteExecutionErrorHandler(testProcessor);
        var startupDurationProcessor = new StartupDurationProcessor();
        this.ctrfJsonComposer = new CtrfJsonComposer(configReader, startupDurationProcessor);
    }

    /**
     * Package-private constructor for testing purposes, allowing dependency injection.
     */
    CtrfReportManager(CtrfReportFileService ctrfReportFileService,
                      TestProcessor testProcessor,
                      SuiteExecutionErrorHandler suiteExecutionErrorHandler,
                      CtrfJsonComposer ctrfJsonComposer) {
        this.ctrfReportFileService = ctrfReportFileService;
        this.testProcessor = testProcessor;
        this.suiteExecutionErrorHandler = suiteExecutionErrorHandler;
        this.ctrfJsonComposer = ctrfJsonComposer;
    }

    public static CtrfReportManager getInstance() {
        return INSTANCE;
    }

    public void onTestStart(TestDetails testDetails) {
        testDetails.setStartTime(System.currentTimeMillis());
        testDetailsMap.put(testDetails.getUniqueId(), testDetails);
    }

    public void onTestSkipped(TestDetails testDetails, Optional<String> reason) {
        long time = System.currentTimeMillis();
        testDetails.setStartTime(time);

        var test = testProcessor.createTest(testDetails.getDisplayName(), testDetails, time);
        test.setStatus(SKIPPED);
        reason.ifPresent(test::setMessage);
        tests.add(test);
    }

    private void processTestResult(String uniqueId, Optional<Throwable> cause, Test.TestStatus status) {
        long stopTime = System.currentTimeMillis();
        TestDetails details = testDetailsMap.remove(uniqueId);
        if (details == null) {
            details = TestDetails.builder().displayName("Unknown Test").startTime(stopTime).build();
        }

        var newTest = testProcessor.createTest(details.getDisplayName(), details, stopTime);
        newTest.setStatus(status);
        cause.ifPresent(c -> testProcessor.setFailureDetails(newTest, c));

        handleRerunsAndFlaky(newTest);
        tests.add(newTest);
    }

    public void onTestSuccess(String uniqueId) {
        processTestResult(uniqueId, Optional.empty(), PASSED);
    }

    public void onTestFailure(String uniqueId, Throwable cause) {
        processTestResult(uniqueId, Optional.ofNullable(cause), FAILED);
    }

    public void onTestAborted(String uniqueId, Throwable cause) {
        processTestResult(uniqueId, Optional.ofNullable(cause), FAILED);
    }

    public void startTestRun() {
        if (isTestRunStarted.compareAndSet(false, true)) {
            Long existingStartTime = ctrfReportFileService.getExistingStartTime();
            testRunStartTime = existingStartTime != null ? existingStartTime : System.currentTimeMillis();
            tests.addAll(ctrfReportFileService.getExistingTests());
        }
    }

    public void finishTestRun(Optional<ExtensionContext> contextOpt) {
        if (!isTestRunStarted.compareAndSet(true, false)) {
            return;
        }

        long testRunStopTime = System.currentTimeMillis();

        if (tests.isEmpty()) {
            contextOpt.ifPresentOrElse(
                context -> suiteExecutionErrorHandler.handleInitializationError(context, testRunStartTime, testRunStopTime)
                    .ifPresent(tests::add),
                () -> { /* Listener has no context for this, can add a synthetic test if needed */ }
            );
        } else if (contextOpt.flatMap(ExtensionContext::getExecutionException).isPresent()) {
            ExtensionContext context = contextOpt.get();
            long lastTestStopTime = tests.get(tests.size() - 1).getStop();
            suiteExecutionErrorHandler.handleExecutionError(context, lastTestStopTime, testRunStopTime).ifPresent(tests::add);
        }

        var summary = SummaryUtil.createSummary(tests, testRunStartTime, testRunStopTime);
        var ctrfJson = ctrfJsonComposer.generateCtrfJson(summary, tests);

        ctrfReportFileService.writeResultsToFile(ctrfJson);
        tests.clear();
    }

    private void handleRerunsAndFlaky(Test newTest) {
        var previousTests = findTestsByName(newTest.getName());
        if (!previousTests.isEmpty()) {
            newTest.setRetries(previousTests.size());
        }

        if (PASSED.equals(newTest.getStatus())) {
            boolean hadPreviousFailures = previousTests.stream()
                .anyMatch(t -> FAILED.equals(t.getStatus()));
            if (hadPreviousFailures || (newTest.getRetries() != null && newTest.getRetries() > 0)) {
                newTest.setFlaky(true);
            }
        }
    }

    private List<Test> findTestsByName(String name) {
        return tests.stream()
            .filter(t -> t.getName() != null && t.getName().equals(name))
            .collect(Collectors.toList());
    }
}