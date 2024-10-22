package io.github.alexshamrai.model;

import io.github.alexshamrai.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestDetailsTest extends BaseTest {

    @Test
    void testBuilderAndGetters() {
        long startTime = System.currentTimeMillis();
        Set<String> tags = Set.of("tag1", "tag2");
        String filePath = "test/file/path";

        TestDetails testDetails = TestDetails.builder()
            .startTime(startTime)
            .tags(tags)
            .filePath(filePath)
            .build();

        assertEquals(startTime, testDetails.getStartTime());
        assertEquals(tags, testDetails.getTags());
        assertEquals(filePath, testDetails.getFilePath());
    }
}