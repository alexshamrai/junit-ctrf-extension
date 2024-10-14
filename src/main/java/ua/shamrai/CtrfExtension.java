package ua.shamrai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.TestWatcher;
import ua.shamrai.model.Results;
import ua.shamrai.model.Summary;
import ua.shamrai.model.Test;
import ua.shamrai.model.Tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CtrfExtension implements Extension, BeforeAllCallback, BeforeEachCallback, InvocationInterceptor, AfterTestExecutionCallback,
    AfterAllCallback, TestWatcher {

    private List<Test> tests;
    private long startTime;
    private long stopTime;

    @Override
    public void beforeAll(ExtensionContext context) {
        tests = new ArrayList<>();
        startTime = System.currentTimeMillis();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        stopTime = System.currentTimeMillis();
        Summary summary = Summary.builder()
            .tests(tests.size())
            .passed((int) tests.stream().filter(t -> t.getStatus() == Test.TestStatus.PASSED).count())
            .failed((int) tests.stream().filter(t -> t.getStatus() == Test.TestStatus.FAILED).count())
            .pending((int) tests.stream().filter(t -> t.getStatus() == Test.TestStatus.PENDING).count())
            .skipped((int) tests.stream().filter(t -> t.getStatus() == Test.TestStatus.SKIPPED).count())
            .other((int) tests.stream().filter(t -> t.getStatus() == Test.TestStatus.OTHER).count())
            .start(startTime)
            .stop(stopTime)
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
    public void testSuccessful(ExtensionContext context) {
        addTestResult(context, Test.TestStatus.PASSED);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        addTestResult(context, Test.TestStatus.FAILED);
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        addTestResult(context, Test.TestStatus.SKIPPED);
    }

    private void addTestResult(ExtensionContext context, Test.TestStatus status) {
        long duration = System.currentTimeMillis() - startTime;
        Test test = Test.builder()
            .name(context.getDisplayName())
            .status(status)
            .duration(duration)
            .build();
        tests.add(test);
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {

    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {

    }
}