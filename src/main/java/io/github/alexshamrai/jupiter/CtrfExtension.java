package io.github.alexshamrai.jupiter;

import io.github.alexshamrai.FileWriter;
import io.github.alexshamrai.ctrf.model.CtrfJson;
import io.github.alexshamrai.ctrf.model.Results;
import io.github.alexshamrai.ctrf.model.Summary;
import io.github.alexshamrai.ctrf.model.Test;
import io.github.alexshamrai.ctrf.model.Tool;
import io.github.alexshamrai.model.TestDetails;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static io.github.alexshamrai.ctrf.model.Test.TestStatus.*;

public class CtrfExtension implements TestRunExtension, BeforeEachCallback, AfterTestExecutionCallback, TestWatcher {

    private static final List<Test> tests = new ArrayList<>();
    private static final int MAX_MESSAGE_LENGTH = 1000;
    private static final String UNKNOWN_CLASS = "UnknownClass";

    private static long testRunStartTime;
    private final ThreadLocal<TestDetails> testDetails = new ThreadLocal<>();
    private final FileWriter fileWriter;

    public CtrfExtension() {
        this.fileWriter = new FileWriter();
    }

    @Override
    public void beforeAllTests(ExtensionContext context) {
        testRunStartTime = System.currentTimeMillis();
    }

    @Override
    public void afterAllTests() {
        long testRunStopTime = System.currentTimeMillis();

        Summary summary = Summary.builder()
            .tests(tests.size())
            .passed((int) tests.stream().filter(t -> t.getStatus() == PASSED).count())
            .failed((int) tests.stream().filter(t -> t.getStatus() == FAILED).count())
            .pending((int) tests.stream().filter(t -> t.getStatus() == PENDING).count())
            .skipped((int) tests.stream().filter(t -> t.getStatus() == SKIPPED).count())
            .other((int) tests.stream().filter(t -> t.getStatus() == OTHER).count())
            .start(testRunStartTime)
            .stop(testRunStopTime)
            .build();

        Results results = Results.builder()
            .tool(Tool.builder().name("JUnit").build())
            .summary(summary)
            .tests(tests)
            .build();

        CtrfJson ctrfJson = CtrfJson.builder()
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
        TestDetails details = testDetails.get();

        Test test = Test.builder()
            .name(context.getDisplayName())
            .tags(new ArrayList<>(details.getTags()))
            .filepath(details.getFilePath())
            .start(details.getStartTime())
            .stop(stopTime)
            .duration(stopTime - details.getStartTime())
            .build();

        tests.add(test);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        updateTestResult(context, PASSED, null);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        updateTestResult(context, FAILED, cause);
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        TestDetails details = createTestDetails(context);
        long stopTime = System.currentTimeMillis();
        Test test = Test.builder()
            .name(context.getDisplayName())
            .tags(new ArrayList<>(details.getTags()))
            .filepath(details.getFilePath())
            .start(details.getStartTime())
            .stop(stopTime)
            .duration(stopTime - details.getStartTime())
            .status(SKIPPED)
            .build();

        tests.add(test);
    }

    private TestDetails createTestDetails(ExtensionContext context) {
        long startTime = System.currentTimeMillis();
        Set<String> tags = context.getTags();
        String filePath = context.getTestClass()
            .map(Class::getName)
            .orElse(UNKNOWN_CLASS);

        return TestDetails.builder()
            .startTime(startTime)
            .tags(tags)
            .filePath(filePath)
            .build();
    }

    private void updateTestResult(ExtensionContext context, Test.TestStatus status, Throwable cause) {
        Test test = tests.stream()
            .filter(t -> t.getName().equals(context.getDisplayName()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Test result not found for context: " + context.getDisplayName()));

        test.setStatus(status);

        if (cause != null) {
            StringWriter sw = new StringWriter();
            cause.printStackTrace(new PrintWriter(sw));
            String trace = sw.toString();
            String message = trace.length() > MAX_MESSAGE_LENGTH ? trace.substring(0, MAX_MESSAGE_LENGTH) : trace;

            test.setMessage(message);
            test.setTrace(trace);
        }
    }
}