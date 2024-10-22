package io.github.alexshamrai.integration;

import org.junit.jupiter.api.Test;

public class LongTest extends BaseIntegrationTest {

    @Test
    void oneSecondTest() throws InterruptedException {
        System.out.println("OneSecondTest");
        Thread.sleep(1000);
    }

    @Test
    void halfSecondTest() throws InterruptedException {
        System.out.println("halfSecondTest");
        Thread.sleep(500);
    }

    @Test
    void twoSecondTest() throws InterruptedException {
        System.out.println("TwoSecondTest");
        Thread.sleep(2000);
    }
}

