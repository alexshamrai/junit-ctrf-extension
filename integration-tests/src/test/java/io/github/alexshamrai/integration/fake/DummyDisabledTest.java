package io.github.alexshamrai.integration.fake;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class DummyDisabledTest extends BaseFakeTest {

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