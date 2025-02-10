package io.github.alexshamrai.util;

import io.github.alexshamrai.BaseTest;
import io.github.alexshamrai.model.TestDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestDetailsUtilTest extends BaseTest {

    @Test
    void createTestDetails_shouldReturnTestDetailsWithProperValues_whenContextHasAllDetails() {
        ExtensionContext mockContext = mock(ExtensionContext.class);
        Set<String> tags = Set.of("unit", "fast");
        String testClassName = "io.github.alexshamrai.util.TestDetailsUtilTest";

        when(mockContext.getTags()).thenReturn(tags);
        when(mockContext.getTestClass()).thenReturn(Optional.of(TestDetailsUtilTest.class)); // Use this test class

        TestDetails testDetails = TestDetailsUtil.createTestDetails(mockContext);

        assertNotNull(testDetails);
        assertEquals(tags, testDetails.getTags());
        assertEquals(testClassName, testDetails.getFilePath());
    }

    @Test
    void createTestDetails_shouldReturnTestDetailsWithUnknownClass_whenContextHasNoTestClass() {
        ExtensionContext mockContext = mock(ExtensionContext.class);
        Set<String> tags = Set.of("integration");

        when(mockContext.getTags()).thenReturn(tags);
        when(mockContext.getTestClass()).thenReturn(Optional.empty());

        TestDetails testDetails = TestDetailsUtil.createTestDetails(mockContext);

        assertNotNull(testDetails);
        assertEquals(tags, testDetails.getTags());
        assertEquals("UnknownClass", testDetails.getFilePath());
    }

    @Test
    void createTestDetails_shouldReturnTestDetailsWithUniqueStartTime() {
        ExtensionContext mockContext = mock(ExtensionContext.class);

        when(mockContext.getTags()).thenReturn(Set.of());
        when(mockContext.getTestClass()).thenReturn(Optional.empty());

        long beforeTime = System.currentTimeMillis();

        TestDetails testDetails = TestDetailsUtil.createTestDetails(mockContext);

        long afterTime = System.currentTimeMillis();

        assertNotNull(testDetails);
        long startTime = testDetails.getStartTime();
        assertTrue(startTime >= beforeTime && startTime <= afterTime);
    }

}