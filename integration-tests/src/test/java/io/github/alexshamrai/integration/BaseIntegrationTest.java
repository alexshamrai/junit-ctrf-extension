package io.github.alexshamrai.integration;

import io.github.alexshamrai.jupiter.CtrfExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(CtrfExtension.class)
public abstract class BaseIntegrationTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("BaseIntegrationTest beforeAll()");
        // throw new RuntimeException("BaseIntegrationTest beforeAll() exception");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("BaseIntegrationTest afterAll()");
        // throw new RuntimeException("BaseIntegrationTest afterAll() exception");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("BaseIntegrationTest beforeEach()");
        // throw new RuntimeException("BaseIntegrationTest beforeEach() exception");
    }

    @AfterEach
    void afterEach() {
        System.out.println("BaseIntegrationTest afterEach()");
        // throw new RuntimeException("BaseIntegrationTest afterEach() exception");
    }
}
