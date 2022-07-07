package ru.job4j.cas;

import org.junit.Test;

import static org.junit.Assert.*;

public class CASCountTest {

    @Test
    public void whenTwoThreadsIncremented() throws InterruptedException {
        CASCount casCount = new CASCount();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 64; i++) {
                casCount.increment();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 64; i++) {
                casCount.increment();
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        assertEquals(128, casCount.get());
    }

}