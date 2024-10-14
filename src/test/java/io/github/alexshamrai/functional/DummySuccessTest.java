package io.github.alexshamrai.functional;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("dummy")
public class DummySuccessTest extends BaseTest {

    @Test
    void firstSuccessTest() {
        System.out.println("DummySuccessTest firstSuccessTest()");
    }

    @Test
    void secondSuccessTest() {
        System.out.println("DummySuccessTest secondSuccessTest()");
    }
}
