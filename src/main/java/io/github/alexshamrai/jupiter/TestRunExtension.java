package io.github.alexshamrai.jupiter;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

// TODO: Add javadoc description
public interface TestRunExtension extends BeforeAllCallback {

    @Override
    default void beforeAll(ExtensionContext context) {
        context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL)
            .getOrComputeIfAbsent(this.getClass(),
                k -> {
                    beforeAllTests(context);
                    return (ExtensionContext.Store.CloseableResource) this::afterAllTests;
                });
    }

    default void beforeAllTests(ExtensionContext context) {

    }

    default void afterAllTests() {

    }
}