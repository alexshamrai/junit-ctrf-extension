package io.github.alexshamrai.integration.fake;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DummyFailedTest extends BaseFakeTest {

    @Test
    void firstFailedTest() throws InterruptedException {
        System.out.println("DummyFailedTest firstFailedTest()");
        Thread.sleep(100);

        assert false;
    }

    @Test
    @DisplayName("Second failed test")
    void secondFailedTest() throws InterruptedException {
        System.out.println("DummyFailedTest secondFailedTest()");
        Thread.sleep(100);
        assert false;
    }
}