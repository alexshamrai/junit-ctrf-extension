package io.github.alexshamrai;

import io.github.alexshamrai.ctrf.model.Test;
import io.github.alexshamrai.model.TestDetails;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import static io.github.alexshamrai.config.ConfigReader.getMaxMessageLength;

public class TestProcessor {

    private static final int MAX_MESSAGE_LENGTH = getMaxMessageLength();

    public void setFailureDetails(Test test, Throwable cause) {
        var stringWriter = new StringWriter();
        cause.printStackTrace(new PrintWriter(stringWriter));
        var trace = stringWriter.toString();
        var message = trace.length() > MAX_MESSAGE_LENGTH ? trace.substring(0, MAX_MESSAGE_LENGTH) + "..." : trace;
        test.setMessage(message);
        test.setTrace(trace);
    }

    public Test createTest(ExtensionContext context, TestDetails details, long stopTime) {
        return Test.builder()
            .name(context.getDisplayName())
            .tags(new ArrayList<>(details.getTags()))
            .filepath(details.getFilePath())
            .start(details.getStartTime())
            .stop(stopTime)
            .duration(stopTime - details.getStartTime())
            .build();
    }
}