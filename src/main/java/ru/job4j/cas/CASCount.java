package ru.job4j.cas;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class CASCount {

    private final AtomicReference<Integer> count = new AtomicReference<>();

    public void increment() {
        int current;
        do {
            current = get();
        } while(!count.compareAndSet(current, current + 1));
    }

    public int get() {
        return count.get();
    }

}
