package io.github.alexshamrai.functional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DummyFailedTest extends BaseTest {

    @Test
    @Disabled
    void firstFailedTest() {
        System.out.println("DummyFailedTest firstFailedTest()");
        assert false;
    }

    @Test
    @Disabled
    @DisplayName("Second failed test")
    void secondFailedTest() {
        System.out.println("DummyFailedTest secondFailedTest()");
        assert false;
    }
}
