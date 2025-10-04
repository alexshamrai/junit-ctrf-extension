package io.github.alexshamrai.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestDetailsUtilTest {

    @Test
    void createTestDetails_shouldReturnTestDetailsWithProperValues_whenContextHasAllDetails() {
        var mockContext = mock(ExtensionContext.class);
        var tags = Set.of("unit", "fast");
        var testClassName = "io.github.alexshamrai.util.TestDetailsUtilTest";

        when(mockContext.getTags()).thenReturn(tags);
        when(mockContext.getTestClass()).thenReturn(Optional.of(TestDetailsUtilTest.class));

        var testDetails = TestDetailsUtil.createTestDetails(mockContext);

        assertNotNull(testDetails);
        assertEquals(tags, testDetails.getTags());
        assertEquals(testClassName, testDetails.getFilePath());
    }

    @Test
    void createTestDetails_shouldReturnTestDetailsWithUnknownClass_whenContextHasNoTestClass() {
        var mockContext = mock(ExtensionContext.class);
        var tags = Set.of("integration");

        when(mockContext.getTags()).thenReturn(tags);
        when(mockContext.getTestClass()).thenReturn(Optional.empty());

        var testDetails = TestDetailsUtil.createTestDetails(mockContext);

        assertNotNull(testDetails);
        assertEquals(tags, testDetails.getTags());
        assertEquals("UnknownClass", testDetails.getFilePath());
    }

    @Test
    void createTestDetails_shouldReturnTestDetailsWithUniqueStartTime() {
        var mockContext = mock(ExtensionContext.class);
        when(mockContext.getTags()).thenReturn(Set.of());
        when(mockContext.getTestClass()).thenReturn(Optional.empty());

        var beforeTime = System.currentTimeMillis();
        var testDetails = TestDetailsUtil.createTestDetails(mockContext);
        var afterTime = System.currentTimeMillis();

        assertNotNull(testDetails);
        var startTime = testDetails.getStartTime();
        assertTrue(startTime >= beforeTime && startTime <= afterTime);
    }

}