package ua.shamrai.functional;

import org.junit.jupiter.api.Test;

public class DummyFailedTest extends BaseTest {

    @Test
    void firstFailedTest() {
        System.out.println("DummyFailedTest firstFailedTest()");
        assert false;
    }

    @Test
    void secondFailedTest() {
        System.out.println("DummyFailedTest secondFailedTest()");
        assert false;
    }
}
