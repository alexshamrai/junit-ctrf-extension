package io.github.alexshamrai.jupiter;

import io.github.alexshamrai.CtrfJsonComposer;
import io.github.alexshamrai.CtrfReportFileService;
import io.github.alexshamrai.StartupDurationProcessor;
import io.github.alexshamrai.SuiteExecutionErrorHandler;
import io.github.alexshamrai.TestProcessor;
import io.github.alexshamrai.config.ConfigReader;
import io.github.alexshamrai.ctrf.model.Test;
import io.github.alexshamrai.model.TestDetails;
import io.github.alexshamrai.util.SummaryUtil;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static io.github.alexshamrai.ctrf.model.Test.TestStatus.FAILED;
import static io.github.alexshamrai.ctrf.model.Test.TestStatus.PASSED;
import static io.github.alexshamrai.ctrf.model.Test.TestStatus.SKIPPED;
import static io.github.alexshamrai.util.TestDetailsUtil.createTestDetails;

/**
 * JUnit 5 extension that generates test reports in the CTRF (Common Test Report Format) format.
 * <p>
 * This extension tracks test execution, captures test results, and generates a JSON report
 * following the CTRF standard. It handles test statuses, and captures relevant test metadata.
 * <p>
 * To use this extension, simply add it to your test class using the {@code @ExtendWith} annotation:
 * <pre>
 * {@code
 * @ExtendWith(CtrfExtension.class)
 * public class MyTest {
 *     // test methods
 * }
 * }
 * </pre>
 * <p>
 * The extension can be configured through a {@code ctrf.properties} file placed in the classpath.
 * See the README for all available configuration options.
 */
public class CtrfExtension implements TestRunExtension, BeforeEachCallback, AfterEachCallback, TestWatcher {

    private static List<Test> tests = new CopyOnWriteArrayList<>();
    private static long testRunStartTime;
    private final ThreadLocal<TestDetails> testDetails = new ThreadLocal<>();
    private final CtrfReportFileService ctrfReportFileService;
    private final TestProcessor testProcessor;
    private final SuiteExecutionErrorHandler suiteExecutionErrorHandler;
    private final CtrfJsonComposer ctrfJsonComposer;

    public CtrfExtension() {
        var configReader = new ConfigReader();
        this.ctrfReportFileService = new CtrfReportFileService(configReader);
        this.testProcessor = new TestProcessor(configReader);
        this.suiteExecutionErrorHandler = new SuiteExecutionErrorHandler(testProcessor);
        var startupDurationProcessor = new StartupDurationProcessor();
        this.ctrfJsonComposer = new CtrfJsonComposer(configReader, startupDurationProcessor);
    }

    @Override
    public void beforeAllTests(ExtensionContext context) {
        Long existingStartTime = ctrfReportFileService.getExistingStartTime();
        testRunStartTime = existingStartTime != null ? existingStartTime : System.currentTimeMillis();
        testDetails.set(createTestDetails(context));
        tests.addAll(ctrfReportFileService.getExistingTests());
    }

    @Override
    public void afterAllTests(ExtensionContext context) {
        var testRunStopTime = System.currentTimeMillis();

        if (tests.isEmpty()) {
            suiteExecutionErrorHandler.handleInitializationError(context, testRunStartTime, testRunStopTime)
                .ifPresent(tests::add);
        } else if (context.getExecutionException().isPresent()) {
            var lastTestStopTime = tests.get(tests.size() - 1).getStop();
            suiteExecutionErrorHandler.handleExecutionError(context, lastTestStopTime, testRunStopTime)
                .ifPresent(tests::add);
        }

        var summary = SummaryUtil.createSummary(tests, testRunStartTime, testRunStopTime);
        var ctrfJson = ctrfJsonComposer.generateCtrfJson(summary, tests);

        ctrfReportFileService.writeResultsToFile(ctrfJson);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        testDetails.set(createTestDetails(context));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        long stopTime = System.currentTimeMillis();
        var details = testDetails.get();

        var newTest = testProcessor.createTest(context, details, stopTime);

        var sameNameTests = findTestsByName(context.getDisplayName());
        if (!sameNameTests.isEmpty()) {
            handleTestRerun(newTest, sameNameTests);
        }

        tests.add(newTest);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        findLatestTestByContext(context).ifPresent(test -> {
            test.setStatus(PASSED);

            var previousTests = findPreviousTestsByName(context.getDisplayName(), test);
            boolean hadPreviousFailures = previousTests.stream()
                .anyMatch(t -> FAILED.equals(t.getStatus()));

            if (hadPreviousFailures || (test.getRetries() != null && test.getRetries() > 0)) {
                test.setFlaky(true);
            }
        });
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        findLatestTestByContext(context).ifPresent(test -> {
            test.setStatus(FAILED);
            testProcessor.setFailureDetails(test, cause);
        });
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        var details = createTestDetails(context);
        long stopTime = System.currentTimeMillis();
        var test = testProcessor.createTest(context, details, stopTime);
        test.setStatus(SKIPPED);
        tests.add(test);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        findTestByContext(context).ifPresent(test -> {
            test.setStatus(FAILED);
            testProcessor.setFailureDetails(test, cause);
        });
    }

    private Optional<Test> findTestByContext(ExtensionContext context) {
        return tests.stream()
            .filter(t -> t.getName() != null && t.getName().equals(context.getDisplayName()))
            .findFirst();
    }

    private Optional<Test> findLatestTestByContext(ExtensionContext context) {
        String displayName = context.getDisplayName();
        return tests.stream()
            .filter(t -> t.getName() != null && t.getName().equals(displayName))
            .reduce((first, second) -> second);
    }

    private List<Test> findTestsByName(String name) {
        return tests.stream()
            .filter(t -> t.getName() != null && t.getName().equals(name))
            .collect(Collectors.toList());
    }

    private List<Test> findPreviousTestsByName(String name, Test currentTest) {
        return tests.stream()
            .filter(t -> t.getName() != null && t.getName().equals(name) && t != currentTest)
            .collect(Collectors.toList());
    }

    private void handleTestRerun(Test newTest, List<Test> previousTests) {
        int retryCount = previousTests.size();
        newTest.setRetries(retryCount);
    }
}
