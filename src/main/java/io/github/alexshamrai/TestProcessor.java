package io.github.alexshamrai;

import io.github.alexshamrai.config.ConfigReader;
import io.github.alexshamrai.ctrf.model.Test;
import io.github.alexshamrai.model.TestDetails;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

@RequiredArgsConstructor
public class TestProcessor {

    private final ConfigReader configReader;

    public void setFailureDetails(Test test, Throwable cause) {
        int maxMessageLength = configReader.getMaxMessageLength();
        var stringWriter = new StringWriter();
        cause.printStackTrace(new PrintWriter(stringWriter));
        var trace = stringWriter.toString();
        var message = trace.length() > maxMessageLength ? trace.substring(0, maxMessageLength) + "..." : trace;
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