package io.github.alexshamrai.functional;

import org.junit.jupiter.api.Test;

public class LongTest extends BaseTest {

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
}

