package io.github.alexshamrai.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TestDetailsTest {

    @Test
    void testBuilder() {
        long startTime = System.currentTimeMillis();
        Set<String> tags = new HashSet<>();
        tags.add("tag1");
        String filePath = "path/to/test/file.java";
        String uniqueId = "id-123";
        String displayName = "My Awesome Test";

        TestDetails testDetails = TestDetails.builder()
            .startTime(startTime)
            .tags(tags)
            .filePath(filePath)
            .uniqueId(uniqueId)
            .displayName(displayName)
            .build();

        assertEquals(startTime, testDetails.getStartTime());
        assertEquals(tags, testDetails.getTags());
        assertEquals(filePath, testDetails.getFilePath());
        assertEquals(uniqueId, testDetails.getUniqueId());
        assertEquals(displayName, testDetails.getDisplayName());
    }

    @Test
    void testSetters() {
        var testDetails = TestDetails.builder().build();
        long startTime = System.currentTimeMillis();
        var tags = new HashSet<String>();
        tags.add("tag2");
        var filePath = "path/to/another/file.java";
        var uniqueId = "id-456";
        var displayName = "Another Great Test";

        testDetails.setStartTime(startTime);
        testDetails.setTags(tags);
        testDetails.setFilePath(filePath);
        testDetails.setUniqueId(uniqueId);
        testDetails.setDisplayName(displayName);

        assertEquals(startTime, testDetails.getStartTime());
        assertEquals(tags, testDetails.getTags());
        assertEquals(filePath, testDetails.getFilePath());
        assertEquals(uniqueId, testDetails.getUniqueId());
        assertEquals(displayName, testDetails.getDisplayName());
    }

    @Test
    void testEqualsAndHashCode() {
        long startTime = System.currentTimeMillis();
        var tags = new HashSet<String>();
        tags.add("tag-equals");
        var filePath = "path/to/test/file.java";
        var uniqueId = "id-789";
        var displayName = "Equals Test";

        var testDetails1 = TestDetails.builder()
            .startTime(startTime)
            .tags(tags)
            .filePath(filePath)
            .uniqueId(uniqueId)
            .displayName(displayName)
            .build();

        var testDetails2 = TestDetails.builder()
            .startTime(startTime)
            .tags(tags)
            .filePath(filePath)
            .uniqueId(uniqueId)
            .displayName(displayName)
            .build();

        var testDetails3 = TestDetails.builder()
            .startTime(startTime + 1000)
            .tags(tags)
            .filePath(filePath)
            .uniqueId(uniqueId)
            .displayName(displayName)
            .build();

        var testDetails4 = TestDetails.builder()
            .startTime(startTime)
            .tags(tags)
            .filePath(filePath)
            .uniqueId("id-different")
            .displayName(displayName)
            .build();

        assertEquals(testDetails1, testDetails2);
        assertEquals(testDetails1.hashCode(), testDetails2.hashCode());

        assertNotEquals(testDetails1, testDetails3);
        assertNotEquals(testDetails1.hashCode(), testDetails3.hashCode());

        assertNotEquals(testDetails1, testDetails4);
        assertNotEquals(testDetails1.hashCode(), testDetails4.hashCode());
    }
}