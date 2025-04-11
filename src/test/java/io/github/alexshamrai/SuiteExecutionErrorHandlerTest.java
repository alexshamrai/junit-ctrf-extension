package io.github.alexshamrai;

import io.github.alexshamrai.ctrf.model.Test;
import io.github.alexshamrai.ctrf.model.Test.TestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

public class SuiteExecutionErrorHandlerTest extends BaseTest {

    @Mock
    private TestProcessor testProcessor;

    @Mock
    private ExtensionContext extensionContext;

    private SuiteExecutionErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        errorHandler = new SuiteExecutionErrorHandler(testProcessor);
    }

    @org.junit.jupiter.api.Test
    void handleInitializationError_withException_returnsTest() {
        var testException = new RuntimeException("Test exception");
        when(extensionContext.getExecutionException()).thenReturn(Optional.of(testException));
        var startTime = System.currentTimeMillis();
        var stopTime = startTime + 1000;

        var result = errorHandler.handleInitializationError(extensionContext, startTime, stopTime);

        assertTrue(result.isPresent());
        var test = result.get();
        assertEquals("Initialization Error", test.getName());
        assertEquals(TestStatus.FAILED, test.getStatus());
        assertEquals(startTime, test.getStart());
        assertEquals(stopTime, test.getStop());
        assertEquals(1000, test.getDuration());

        var testCaptor = ArgumentCaptor.forClass(Test.class);
        verify(testProcessor).setFailureDetails(testCaptor.capture(), eq(testException));
        assertEquals(test, testCaptor.getValue());
    }

    @org.junit.jupiter.api.Test
    void handleInitializationError_withoutException_returnsEmptyOptional() {
        when(extensionContext.getExecutionException()).thenReturn(Optional.empty());
        var startTime = System.currentTimeMillis();
        var stopTime = startTime + 1000;

        var result = errorHandler.handleInitializationError(extensionContext, startTime, stopTime);

        assertFalse(result.isPresent());
        verify(testProcessor, never()).setFailureDetails(any(), any());
    }

    @org.junit.jupiter.api.Test
    void handleExecutionError_withException_returnsTest() {
        var testException = new RuntimeException("Test exception");
        when(extensionContext.getExecutionException()).thenReturn(Optional.of(testException));
        var startTime = System.currentTimeMillis();
        var stopTime = startTime + 1000;

        var result = errorHandler.handleExecutionError(extensionContext, startTime, stopTime);

        assertTrue(result.isPresent());
        Test test = result.get();
        assertEquals("Execution Error", test.getName());
        assertEquals(TestStatus.FAILED, test.getStatus());
        assertEquals(startTime, test.getStart());
        assertEquals(stopTime, test.getStop());
        assertEquals(1000, test.getDuration());

        var testCaptor = ArgumentCaptor.forClass(Test.class);
        verify(testProcessor).setFailureDetails(testCaptor.capture(), eq(testException));
        assertEquals(test, testCaptor.getValue());
    }

    @org.junit.jupiter.api.Test
    void handleExecutionError_withoutException_returnsEmptyOptional() {
        when(extensionContext.getExecutionException()).thenReturn(Optional.empty());
        var startTime = System.currentTimeMillis();
        var stopTime = startTime + 1000;

        var result = errorHandler.handleExecutionError(extensionContext, startTime, stopTime);

        assertFalse(result.isPresent());
        verify(testProcessor, never()).setFailureDetails(any(), any());
    }
}
