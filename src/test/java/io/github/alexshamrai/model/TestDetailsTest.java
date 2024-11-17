package io.github.alexshamrai.model;

import io.github.alexshamrai.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TestDetailsTest extends BaseTest {

    @Test
    void testBuilder() {
        var startTime = System.currentTimeMillis();
        var tags = new HashSet<String>();
        tags.add("tag1");
        tags.add("tag2");
        String filePath = "path/to/test/file.java";

        TestDetails testDetails = TestDetails.builder()
                .startTime(startTime)
                .tags(tags)
                .filePath(filePath)
                .build();

        assertEquals(startTime, testDetails.getStartTime());
        assertEquals(tags, testDetails.getTags());
        assertEquals(filePath, testDetails.getFilePath());
    }

    @Test
    void testSetters() {
        var testDetails = TestDetails.builder().build();
        var startTime = System.currentTimeMillis();
        var tags = new HashSet<String>();
        tags.add("tag1");
        tags.add("tag2");
        var filePath = "path/to/test/file.java";

        testDetails.setStartTime(startTime);
        testDetails.setTags(tags);
        testDetails.setFilePath(filePath);

        assertEquals(startTime, testDetails.getStartTime());
        assertEquals(tags, testDetails.getTags());
        assertEquals(filePath, testDetails.getFilePath());
    }

    @Test
    void testEqualsAndHashCode() {
        var startTime = System.currentTimeMillis();
        var tags = new HashSet<String>();
        tags.add("tag1");
        tags.add("tag2");
        var filePath = "path/to/test/file.java";

        TestDetails testDetails1 = TestDetails.builder()
                .startTime(startTime)
                .tags(tags)
                .filePath(filePath)
                .build();

        var testDetails2 = TestDetails.builder()
                .startTime(startTime)
                .tags(tags)
                .filePath(filePath)
                .build();

        var testDetails3 = TestDetails.builder()
                .startTime(startTime + 1000)
                .tags(tags)
                .filePath(filePath)
                .build();

        assertEquals(testDetails1, testDetails2);
        assertEquals(testDetails1.hashCode(), testDetails2.hashCode());
        assertNotEquals(testDetails1, testDetails3);
        assertNotEquals(testDetails1.hashCode(), testDetails3.hashCode());
    }
}