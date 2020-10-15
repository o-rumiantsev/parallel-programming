package com.lab3;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        int ARRAY_SIZE = 10;

        int[] array = new int [ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = new Random().nextInt();
        }
        System.out.println(Arrays.toString(array));

        int count = countElements(array, value -> value > 0);
        System.out.println("Count: " + count);

        int[][] minMax = getMinMax(array);
        System.out.println("Min value: " + minMax[0][0] + ", Index: " + minMax[0][1]);
        System.out.println("Max value: " + minMax[1][0] + ", Index: " + minMax[1][1]);

        int controlSum = calculateControlSum(array);
        System.out.println("Control sum: " + controlSum);
    }
    
    public static int countElements(int[] array, CountPredicate countPredicate) {
        Counter count = new Counter();

        IntStream.of(array).parallel().forEach(item -> {
            if (countPredicate.predicate(item)) {
                count.increment();
            }
        });

        return count.getValue();
    }

    public static int[][] getMinMax(int[] array) {
        AtomicInteger minIndex = new AtomicInteger(0);
        AtomicInteger maxIndex = new AtomicInteger(0);

        IntStream.range(0, array.length).parallel().forEach(index -> {
            int minI;
            int maxI;

            do {
                minI = minIndex.get();
                if (array[index] > array[minI]) {
                    break;
                }
            } while (!minIndex.compareAndSet(minI, index));

            do {
                maxI = maxIndex.get();
                if (array[index] < maxI) {
                    break;
                }
            } while (!maxIndex.compareAndSet(maxI, index));
        });

        return new int[][]{
                {array[minIndex.get()], minIndex.get()},
                {array[maxIndex.get()], maxIndex.get()},
        };
    }

    public static int calculateControlSum(int[] array) {
        AtomicInteger controlSum = new AtomicInteger(0);

        IntStream.of(array).parallel().forEach(item -> {
            int sumValue;
            do {
                sumValue = controlSum.get();
            } while (!controlSum.compareAndSet(sumValue, sumValue ^ item));
        });

        return controlSum.get();
    }
}
