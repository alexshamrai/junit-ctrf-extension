package io.github.alexshamrai.jupiter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.*;
import io.github.alexshamrai.ctrf.model.Results;
import io.github.alexshamrai.ctrf.model.Summary;
import io.github.alexshamrai.ctrf.model.Test;
import io.github.alexshamrai.ctrf.model.Tool;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static io.github.alexshamrai.ctrf.model.Test.TestStatus.FAILED;
import static io.github.alexshamrai.ctrf.model.Test.TestStatus.OTHER;
import static io.github.alexshamrai.ctrf.model.Test.TestStatus.PASSED;
import static io.github.alexshamrai.ctrf.model.Test.TestStatus.PENDING;
import static io.github.alexshamrai.ctrf.model.Test.TestStatus.SKIPPED;

public class CtrfExtension implements TestRunExtension, BeforeEachCallback, AfterTestExecutionCallback, TestWatcher {

    private static final List<Test> tests = new ArrayList<>();
    private static final int MAX_MESSAGE_LENGTH = 1000;

    private static long testRunStartTime;
    private ThreadLocal<TestDetails> testDetails = new ThreadLocal<>();

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

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("ctrf-report.json"), results);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        long startTime = System.currentTimeMillis();
        Set<String> tags = context.getTags();
        String filePath = context.getTestClass()
            .map(Class::getName)
            .orElse("UnknownClass");

        testDetails.set(new TestDetails(startTime, tags, filePath));
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
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
        updateTestResult(context, SKIPPED, null);
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

    private static class TestDetails {
        private final long startTime;
        private final Set<String> tags;
        private final String filePath;

        public TestDetails(long startTime, Set<String> tags, String filePath) {
            this.startTime = startTime;
            this.tags = new HashSet<>(tags);
            this.filePath = filePath;
        }

        public long getStartTime() {
            return startTime;
        }

        public Set<String> getTags() {
            return tags;
        }

        public String getFilePath() {
            return filePath;
        }
    }
}