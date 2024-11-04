package io.github.alexshamrai.jupiter;

import io.github.alexshamrai.CtrfJsonComposer;
import io.github.alexshamrai.ctrf.model.Test;
import io.github.alexshamrai.model.TestDetails;
import io.github.alexshamrai.SuiteExecutionErrorHandler;
import io.github.alexshamrai.FileWriter;
import io.github.alexshamrai.util.SummaryUtil;
import io.github.alexshamrai.TestProcessor;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.github.alexshamrai.ctrf.model.Test.TestStatus.FAILED;
import static io.github.alexshamrai.ctrf.model.Test.TestStatus.PASSED;
import static io.github.alexshamrai.ctrf.model.Test.TestStatus.SKIPPED;
import static io.github.alexshamrai.util.TestDetailsUtil.createTestDetails;

public class CtrfExtension implements TestRunExtension, BeforeEachCallback, AfterEachCallback, TestWatcher {

    private static final List<Test> tests = new ArrayList<>();
    private static long testRunStartTime;
    private final ThreadLocal<TestDetails> testDetails = new ThreadLocal<>();
    private final FileWriter fileWriter;
    private final TestProcessor testProcessor;
    private final SuiteExecutionErrorHandler suiteExecutionErrorHandler;

    public CtrfExtension() {
        this.fileWriter = new FileWriter();
        this.testProcessor = new TestProcessor();
        this.suiteExecutionErrorHandler = new SuiteExecutionErrorHandler(testProcessor);
    }

    @Override
    public void beforeAllTests(ExtensionContext context) {
        testRunStartTime = System.currentTimeMillis();
        testDetails.set(createTestDetails(context));
    }

    @Override
    public void afterAllTests(ExtensionContext context) {
        var testRunStopTime = System.currentTimeMillis();

        if (tests.isEmpty()) {
            suiteExecutionErrorHandler.handleInitializationError(context, testRunStartTime, testRunStopTime)
                .ifPresent(tests::add);
        } else if (context.getExecutionException().isPresent()) {
            var lastTestStopTime = tests.getLast().getStop();
            suiteExecutionErrorHandler.handleExecutionError(context, lastTestStopTime, testRunStopTime)
                .ifPresent(tests::add);
        }

        var summary = SummaryUtil.createSummary(tests, testRunStartTime, testRunStopTime);
        var ctrfJson = CtrfJsonComposer.generateCtrfJson(summary, tests);

        fileWriter.writeResultsToFile(ctrfJson);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        testDetails.set(createTestDetails(context));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        long stopTime = System.currentTimeMillis();
        var details = testDetails.get();

        var test = testProcessor.createTest(context, details, stopTime);
        tests.add(test);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        findTestByContext(context).ifPresent(test -> test.setStatus(PASSED));
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        findTestByContext(context).ifPresent(test -> {
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
            .filter(t -> t.getName().equals(context.getDisplayName()))
            .findFirst();
    }
}