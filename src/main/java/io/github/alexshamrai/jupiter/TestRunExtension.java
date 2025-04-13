package io.github.alexshamrai.jupiter;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Interface that allows extensions to execute code at the beginning and end of the entire test run.
 * <p>
 * This interface uses JUnit's extension model to reliably execute code exactly once at the start
 * of the test execution and once at the end, regardless of parallel execution or test nesting.
 * <p>
 * Extensions implementing this interface can use it to set up global resources before tests start
 * executing and to tear down or output reports after all tests have completed.
 */
public interface TestRunExtension extends BeforeAllCallback {

    @Override
    default void beforeAll(ExtensionContext context) {
        context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL)
            .getOrComputeIfAbsent(this.getClass(),
                k -> {
                    beforeAllTests(context);
                    return new ExtensionContext.Store.CloseableResource() {
                        @Override
                        public void close() {
                            afterAllTests(context);
                        }
                    };
                });
    }

    /**
     * Executed exactly once before any test in the test plan is run.
     * <p>
     * This method is suitable for initial setup work that should happen only once
     * for the entire test execution, such as initializing a report, starting timers,
     * or setting up global resources.
     *
     * @param context the current extension context
     */
    default void beforeAllTests(ExtensionContext context) {

    }

    /**
     * Executed exactly once after all tests in the test plan have completed.
     * <p>
     * This method is suitable for cleanup work that should happen only once
     * at the end of the entire test execution, such as finalizing and writing reports,
     * collecting statistics, or releasing global resources.
     *
     * @param context the current extension context
     */
    default void afterAllTests(ExtensionContext context) {

    }
}
