package io.github.alexshamrai.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DummyFailedTest extends BaseIntegrationTest {

    @Test
    void firstFailedTest() {
        System.out.println("DummyFailedTest firstFailedTest()");
        assert false;
    }

    @Test
    @DisplayName("Second failed test")
    void secondFailedTest() {
        System.out.println("DummyFailedTest secondFailedTest()");
        assert false;
    }
}
