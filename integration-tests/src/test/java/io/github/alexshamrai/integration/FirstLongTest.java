package io.github.alexshamrai.integration;

import org.junit.jupiter.api.Test;

public class FirstLongTest extends BaseIntegrationTest {

    @Test
    void firstLongOneSecondTest() throws InterruptedException {
        System.out.println("OneSecondTest");
        Thread.sleep(1000);
    }

    @Test
    void firstLongHalfSecondTest() throws InterruptedException {
        System.out.println("halfSecondTest");
        Thread.sleep(500);
    }

    @Test
    void firstLongTwoSecondTest() throws InterruptedException {
        System.out.println("TwoSecondTest");
        Thread.sleep(2000);
    }
}
