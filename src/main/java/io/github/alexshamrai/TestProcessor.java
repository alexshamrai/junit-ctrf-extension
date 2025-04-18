package io.github.alexshamrai;

import io.github.alexshamrai.config.ConfigReader;
import io.github.alexshamrai.ctrf.model.Test;
import io.github.alexshamrai.model.TestDetails;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Processes test-related information to create and enhance test result objects.
 * 
 * <p>This class is responsible for creating test objects that conform to the Common Test
 * Reporting Format (CTRF) specification, adding failure details when tests fail, and
 * enriching test objects with additional metadata.</p>
 */
@RequiredArgsConstructor
public class TestProcessor {

    private final ConfigReader configReader;

    /**
     * Adds failure details to a test that has failed.
     * 
     * <p>Captures the exception stack trace and message, applying configured length limitations
     * to prevent excessive message sizes.</p>
     *
     * @param test the Test object to which failure details will be added
     * @param cause the Throwable that caused the test failure
     */
    public void setFailureDetails(Test test, Throwable cause) {
        int maxMessageLength = configReader.getMaxMessageLength();
        var stringWriter = new StringWriter();
        cause.printStackTrace(new PrintWriter(stringWriter));
        var trace = stringWriter.toString();
        var message = trace.length() > maxMessageLength ? trace.substring(0, maxMessageLength) + "..." : trace;
        test.setMessage(message);
        test.setTrace(trace);
    }

    /**
     * Creates a new Test object with details from the test execution context.
     * 
     * <p>Builds a CTRF-compliant Test object containing all relevant metadata about the test,
     * including name, tags, file path, execution times, and additional information like thread ID.</p>
     *
     * @param context the JUnit extension context containing test information
     * @param details additional test details gathered during execution
     * @param stopTime the timestamp when the test completed
     * @return a fully populated Test object
     */
    public Test createTest(ExtensionContext context, TestDetails details, long stopTime) {
        return Test.builder()
            .name(context.getDisplayName())
            .tags(new ArrayList<>(details.getTags()))
            .filepath(details.getFilePath())
            .start(details.getStartTime())
            .stop(stopTime)
            .duration(stopTime - details.getStartTime())
            .threadId(Thread.currentThread().getName())
            .build();
    }
}
