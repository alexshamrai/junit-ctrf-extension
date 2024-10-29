package io.github.alexshamrai;

import io.github.alexshamrai.ctrf.model.Test;
import io.github.alexshamrai.ctrf.model.Test.TestStatus;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Optional;

public class SuiteExecutionErrorHandler {

    private final TestProcessor testProcessor;

    public SuiteExecutionErrorHandler(TestProcessor testProcessor) {
        this.testProcessor = testProcessor;
    }

    public Optional<Test> handleInitializationError(ExtensionContext context, long testRunStartTime, long testRunStopTime) {
        return handleError(context, "Initialization Error", testRunStartTime, testRunStopTime);
    }

    public Optional<Test> handleExecutionError(ExtensionContext context, long testRunStartTime, long testRunStopTime) {
        return handleError(context, "Execution Error", testRunStartTime, testRunStopTime);
    }

    private Optional<Test> handleError(ExtensionContext context, String errorName, long testRunStartTime, long testRunStopTime) {
        return context.getExecutionException().map(cause -> {
            var failureTest = Test.builder()
                .name(errorName)
                .status(TestStatus.FAILED)
                .start(testRunStartTime)
                .stop(testRunStopTime)
                .duration(testRunStopTime - testRunStartTime)
                .build();
            testProcessor.setFailureDetails(failureTest, cause);
            return failureTest;
        });
    }
}