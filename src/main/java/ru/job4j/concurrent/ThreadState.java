package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> {
                    System.out.println(Thread.currentThread().getName());
                }
        );
        System.out.printf("First: %s \n", first.getState());
        first.start();
        Thread second = new Thread(
                () -> {
                    System.out.println(Thread.currentThread().getName());
                }
        );
        System.out.printf("Second: %s \n", second.getState());
        second.start();
        while (first.getState() != Thread.State.TERMINATED || second.getState() != Thread.State.TERMINATED) {
            System.out.printf("First: %s \n", first.getState());
            System.out.printf("Second: %s \n", second.getState());
        }
        System.out.printf("First: %s \n", first.getState());
        System.out.printf("Second: %s \n", second.getState());
    }
}
