package ru.job4j.pools;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinValue<T> extends RecursiveTask<Integer> {

    private T value;
    private T[] array;
    private final int lengthForLinear = 10;

    public ForkJoinValue(T value, T[] array) {
        this.value = value;
        this.array = array;
    }

    @Override
    protected Integer compute() {
        int result = -1;
        if (array.length <= lengthForLinear) {
            result = computeLinear();
        }

        return result;
    }

    private Integer computeLinear() {
        int result = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                result = i;
                break;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int value = 10;
        Integer[] array = new Integer[100];
        for (int i = 0; i < 100; i++) {
            array[i] = i;
        }
        int index = new ForkJoinPool().invoke(new ForkJoinValue<>(value, array));
    }
}