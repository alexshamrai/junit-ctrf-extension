package io.github.alexshamrai.jupiter;

import io.github.alexshamrai.util.FileWriter;
import io.github.alexshamrai.util.SummaryCreator;
import io.github.alexshamrai.ctrf.model.CtrfJson;
import io.github.alexshamrai.ctrf.model.Results;
import io.github.alexshamrai.ctrf.model.Test;
import io.github.alexshamrai.ctrf.model.Tool;
import io.github.alexshamrai.model.TestDetails;
import io.github.alexshamrai.util.TestProcessor;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
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

public class CtrfExtension implements TestRunExtension, BeforeEachCallback, AfterTestExecutionCallback, TestWatcher {

    private static final List<Test> tests = new ArrayList<>();
    private static long testRunStartTime;
    private final ThreadLocal<TestDetails> testDetails = new ThreadLocal<>();
    private final FileWriter fileWriter;
    private final SummaryCreator summaryCreator;
    private final TestProcessor testProcessor;

    public CtrfExtension() {
        this.fileWriter = new FileWriter();
        this.summaryCreator = new SummaryCreator();
        this.testProcessor = new TestProcessor();
    }

    @Override
    public void beforeAllTests(ExtensionContext context) {
        testRunStartTime = System.currentTimeMillis();
    }

    @Override
    public void afterAllTests() {
        var testRunStopTime = System.currentTimeMillis();
        var summary = summaryCreator.createSummary(tests, testRunStartTime, testRunStopTime);

        var results = Results.builder()
            .tool(Tool.builder().name("JUnit").build())
            .summary(summary)
            .tests(tests)
            .build();
        var ctrfJson = CtrfJson.builder()
            .results(results)
            .build();

        fileWriter.writeResultsToFile(ctrfJson);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        testDetails.set(createTestDetails(context));
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
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