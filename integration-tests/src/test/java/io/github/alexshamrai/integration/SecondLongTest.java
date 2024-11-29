package io.github.alexshamrai.integration;

import org.junit.jupiter.api.Test;

public class SecondLongTest extends BaseIntegrationTest {

    @Test
    void secondLongOneSecondTest() throws InterruptedException {
        System.out.println("OneSecondTest");
        Thread.sleep(1000);
    }

    @Test
    void secondLongHalfSecondTest() throws InterruptedException {
        System.out.println("halfSecondTest");
        Thread.sleep(500);
    }

    @Test
    void xecondLongTwoSecondTest() throws InterruptedException {
        System.out.println("TwoSecondTest");
        Thread.sleep(2000);
    }
}

