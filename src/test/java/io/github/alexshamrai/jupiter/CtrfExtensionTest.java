package io.github.alexshamrai.jupiter;

import io.github.alexshamrai.CtrfJsonComposer;
import io.github.alexshamrai.CtrfReportFileService;
import io.github.alexshamrai.SuiteExecutionErrorHandler;
import io.github.alexshamrai.TestProcessor;
import io.github.alexshamrai.ctrf.model.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Optional;

import static io.github.alexshamrai.ctrf.model.Test.TestStatus.FAILED;
import static io.github.alexshamrai.ctrf.model.Test.TestStatus.PASSED;
import static io.github.alexshamrai.ctrf.model.Test.TestStatus.SKIPPED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CtrfExtensionTest {

    @Mock
    private CtrfReportFileService ctrfReportFileService;

    @Mock
    private TestProcessor testProcessor;

    @Mock
    private SuiteExecutionErrorHandler suiteExecutionErrorHandler;

    @Mock
    private CtrfJsonComposer ctrfJsonComposer;

    @Mock
    private ExtensionContext extensionContext;

    private CtrfExtension ctrfExtension;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        ctrfExtension = new CtrfExtension();

        setPrivateField(ctrfExtension, "ctrfReportFileService", ctrfReportFileService);
        setPrivateField(ctrfExtension, "testProcessor", testProcessor);
        setPrivateField(ctrfExtension, "suiteExecutionErrorHandler", suiteExecutionErrorHandler);
        setPrivateField(ctrfExtension, "ctrfJsonComposer", ctrfJsonComposer);

        resetTestsField();

        when(extensionContext.getDisplayName()).thenReturn("Test Display Name");
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        var field = CtrfExtension.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private void resetTestsField() throws Exception {
        var testsField = CtrfExtension.class.getDeclaredField("tests");
        testsField.setAccessible(true);
        testsField.set(null, new java.util.concurrent.CopyOnWriteArrayList<>());
    }

    @org.junit.jupiter.api.Test
    void testBeforeAllTests() {
        var existingTests = new ArrayList<Test>();
        when(ctrfReportFileService.getExistingTests()).thenReturn(existingTests);

        ctrfExtension.beforeAllTests(extensionContext);

        verify(ctrfReportFileService).getExistingTests();

        try {
            Field testRunStartTimeField = CtrfExtension.class.getDeclaredField("testRunStartTime");
            testRunStartTimeField.setAccessible(true);
            long testRunStartTime = (long) testRunStartTimeField.get(null);
            assertTrue(testRunStartTime > 0, "testRunStartTime should be set to a non-zero value");
        } catch (Exception e) {
            throw new RuntimeException("Failed to access testRunStartTime field", e);
        }
    }

    @org.junit.jupiter.api.Test
    void testAfterAllTestsWithEmptyTests() {
        var errorTest = Optional.of(Test.builder().build());
        when(suiteExecutionErrorHandler.handleInitializationError(eq(extensionContext), anyLong(), anyLong()))
            .thenReturn(errorTest);
        when(ctrfJsonComposer.generateCtrfJson(any(), any())).thenReturn(null);

        ctrfExtension.afterAllTests(extensionContext);

        verify(suiteExecutionErrorHandler).handleInitializationError(eq(extensionContext), anyLong(), anyLong());
        verify(ctrfJsonComposer).generateCtrfJson(any(), any());
        verify(ctrfReportFileService).writeResultsToFile(any());
    }

    @org.junit.jupiter.api.Test
    void testAfterAllTestsWithExecutionException() {
        var mockTest = Test.builder().stop(1000L).build();
        when(testProcessor.createTest(eq(extensionContext), any(), anyLong())).thenReturn(mockTest);
        ctrfExtension.beforeEach(extensionContext);
        ctrfExtension.afterEach(extensionContext);

        when(extensionContext.getExecutionException()).thenReturn(Optional.of(new RuntimeException("Test exception")));

        var errorTest = Optional.of(Test.builder().build());
        when(suiteExecutionErrorHandler.handleExecutionError(eq(extensionContext), anyLong(), anyLong()))
            .thenReturn(errorTest);
        when(ctrfJsonComposer.generateCtrfJson(any(), any())).thenReturn(null);

        ctrfExtension.afterAllTests(extensionContext);

        verify(suiteExecutionErrorHandler).handleExecutionError(eq(extensionContext), anyLong(), anyLong());
        verify(ctrfJsonComposer).generateCtrfJson(any(), any());
        verify(ctrfReportFileService).writeResultsToFile(any());
    }


    @org.junit.jupiter.api.Test
    void testAfterEach() {
        var mockTest = Test.builder().name("Test Display Name").build();
        when(testProcessor.createTest(eq(extensionContext), any(), anyLong())).thenReturn(mockTest);

        ctrfExtension.beforeEach(extensionContext);
        ctrfExtension.afterEach(extensionContext);

        verify(testProcessor).createTest(eq(extensionContext), any(), anyLong());
    }

    @org.junit.jupiter.api.Test
    void testTestSuccessful() {
        var mockTest = Test.builder().name("Test Display Name").build();
        when(testProcessor.createTest(eq(extensionContext), any(), anyLong())).thenReturn(mockTest);

        ctrfExtension.beforeEach(extensionContext);
        ctrfExtension.afterEach(extensionContext);

        ctrfExtension.testSuccessful(extensionContext);

        assertEquals(PASSED, mockTest.getStatus());
    }

    @org.junit.jupiter.api.Test
    void testTestSuccessfulWithRetries() {
        Test mockTest = Test.builder().name("Test Display Name").retries(1).build();
        when(testProcessor.createTest(eq(extensionContext), any(), anyLong())).thenReturn(mockTest);

        ctrfExtension.beforeEach(extensionContext);
        ctrfExtension.afterEach(extensionContext);

        ctrfExtension.testSuccessful(extensionContext);

        assertEquals(PASSED, mockTest.getStatus());
        assertTrue(mockTest.getFlaky());
    }

    @org.junit.jupiter.api.Test
    void testTestFailed() {
        var mockTest = Test.builder().name("Test Display Name").build();
        when(testProcessor.createTest(eq(extensionContext), any(), anyLong())).thenReturn(mockTest);
        var cause = new RuntimeException("Test failed");

        ctrfExtension.beforeEach(extensionContext);
        ctrfExtension.afterEach(extensionContext);

        ctrfExtension.testFailed(extensionContext, cause);

        assertEquals(FAILED, mockTest.getStatus());
        verify(testProcessor).setFailureDetails(mockTest, cause);
    }

    @org.junit.jupiter.api.Test
    void testTestDisabled() {
        var mockTest = Test.builder().name("Test Display Name").build();
        when(testProcessor.createTest(eq(extensionContext), any(), anyLong())).thenReturn(mockTest);
        Optional<String> reason = Optional.of("Test disabled reason");

        ctrfExtension.testDisabled(extensionContext, reason);

        assertEquals(SKIPPED, mockTest.getStatus());
        verify(testProcessor).createTest(eq(extensionContext), any(), anyLong());
    }

    @org.junit.jupiter.api.Test
    void testTestAborted() {
        var mockTest = Test.builder().name("Test Display Name").build();
        when(testProcessor.createTest(eq(extensionContext), any(), anyLong())).thenReturn(mockTest);
        var cause = new RuntimeException("Test aborted");

        ctrfExtension.beforeEach(extensionContext);
        ctrfExtension.afterEach(extensionContext);

        ctrfExtension.testAborted(extensionContext, cause);

        assertEquals(FAILED, mockTest.getStatus());
        verify(testProcessor).setFailureDetails(mockTest, cause);
    }

    @org.junit.jupiter.api.Test
    void testHandleTestRerun() {
        var firstTest = Test.builder()
            .name("Test Display Name")
            .message("Original message")
            .trace("Original trace")
            .build();

        var secondTest = Test.builder()
            .name("Test Display Name")
            .build();

        when(testProcessor.createTest(eq(extensionContext), any(), anyLong()))
            .thenReturn(firstTest)
            .thenReturn(secondTest);

        ctrfExtension.beforeEach(extensionContext);
        ctrfExtension.afterEach(extensionContext);

        ctrfExtension.beforeEach(extensionContext);
        ctrfExtension.afterEach(extensionContext);

        assertEquals(1, secondTest.getRetries());
        assertEquals("Original message", secondTest.getMessage());
        assertEquals("Original trace", secondTest.getTrace());
    }
}
