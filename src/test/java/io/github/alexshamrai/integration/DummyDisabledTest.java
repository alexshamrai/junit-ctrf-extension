package io.github.alexshamrai.integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class DummyDisabledTest extends BaseIntegrationTest {

    @Test
    @Disabled
    void firstDisabledTest() {
        System.out.println("DummyDisabledTest firstFailedTest()");
    }

    @Test
    @Disabled
    void secondDisabledTest() {
        System.out.println("DummyDisabledTest secondDisabledTest()");
    }
}
