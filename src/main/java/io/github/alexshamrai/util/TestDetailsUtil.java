package io.github.alexshamrai.util;

import io.github.alexshamrai.model.TestDetails;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Set;

public class TestDetailsUtil {

    private static final String UNKNOWN_CLASS = "UnknownClass";

    public static TestDetails createTestDetails(ExtensionContext context) {
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
}