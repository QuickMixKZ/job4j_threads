package ru.job4j.synch;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleBlockingQueueTest {

    @Test
    public void whenTenOfferedAndTenPolled() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                queue.offer(i);
            }
        });
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Integer result = queue.poll();
                assertEquals(Integer.valueOf(i), result);
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }
}