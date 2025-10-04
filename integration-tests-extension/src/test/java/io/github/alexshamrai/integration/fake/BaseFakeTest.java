package io.github.alexshamrai.integration.fake;

import io.github.alexshamrai.jupiter.CtrfExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(CtrfExtension.class)
public abstract class BaseFakeTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("BaseIntegrationTest beforeAll()");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("BaseIntegrationTest afterAll()");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("BaseIntegrationTest beforeEach()");
    }

    @AfterEach
    void afterEach() {
        System.out.println("BaseIntegrationTest afterEach()");
    }
}
