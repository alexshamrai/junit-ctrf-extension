package io.github.alexshamrai.integration;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("dummy")
public class DummySuccessTest extends BaseIntegrationTest {

    @Test
    void firstSuccessTest() {
        System.out.println("DummySuccessTest firstSuccessTest()");
    }

    @Test
    void secondSuccessTest() {
        System.out.println("DummySuccessTest secondSuccessTest()");
    }
}
