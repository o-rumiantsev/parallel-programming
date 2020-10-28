package com.lab5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    static final int MAX_ITEM = 100;
    static final int MIN_ITEM = 0;
    static final int ARRAY_SIZE = 100;
    static final int ARRAYS_COUNT = 3;

    public static void main(String[] args)
            throws ExecutionException, InterruptedException {
        int[][] arrays = new int[ARRAYS_COUNT][];
        for (int i = 0; i < ARRAYS_COUNT; ++i) {
            arrays[i] = randomArray(ARRAY_SIZE);
        }

        List<CompletableFuture<int[]>> futuresList = new ArrayList<>();
        futuresList.add(CompletableFuture
                .supplyAsync(() -> changeFirst(arrays[0])));
        futuresList.add(CompletableFuture
                .supplyAsync(() -> changeSecond(arrays[1])));
        futuresList.add(CompletableFuture
                .supplyAsync(() -> changeThird(arrays[2])));

        CompletableFuture<List<Integer>> allFutures =
                CompletableFuture.allOf(
                    futuresList.toArray(new CompletableFuture[0])
                ).thenApply((i) -> {
                    List<Integer> result = new ArrayList<>();
                    List<int[]> results = futuresList.stream()
                            .map(future -> future.join())
                            .collect(Collectors.toList());

                    results.forEach(arr -> {
                        System.out.println(Arrays.toString(arr));
                        Arrays.stream(arr).forEach(item -> {
                            if (allContain(results, item)) {
                                result.add(item);
                            }
                        });
                    });

                    result.sort(Comparator.comparingInt(a -> a));
                    return result;
                });

        System.out.println(allFutures.get());
    }

    public static int[] randomArray(int size) {
        return IntStream
                .range(0, size)
                .map((i) -> randomInt())
                .toArray();
    }

    public static int randomInt() {
        int scale = MAX_ITEM - MIN_ITEM;
        return (int) Math.floor((Math.random() * scale) + MIN_ITEM);
    }

    public static boolean allContain(List<int[]> arrays, int item) {
        return arrays.stream().allMatch(array -> contains(array, item));
    }

    public static boolean contains(int[] array, int value) {
        for (int item: array) {
            if (item == value) {
                return true;
            }
        }
        return false;
    }

    public static int[] changeFirst(int[] array) {
        try {
            Thread.sleep((int) (Math.random() * 100));
        } catch (InterruptedException e) {
            e.printStackTrace();
            return array;
        }
        return IntStream.of(array).map(a -> a * 5).sorted().toArray();
    }

    public static int[] changeSecond(int[] array) {
        try {
            Thread.sleep((int) (Math.random() * 100));
        } catch (InterruptedException e) {
            e.printStackTrace();
            return array;
        }
        return IntStream.of(array)
                .filter(i -> i % 2 == 0)
                .sorted()
                .toArray();
    }

    public static int[] changeThird(int[] array) {
        try {
            Thread.sleep((int) (Math.random() * 100));
        } catch (InterruptedException e) {
            e.printStackTrace();
            return array;
        }
        int maxItem = IntStream.of(array).max().orElse(0);
        double max = maxItem * 0.6;
        double min = maxItem * 0.4;
        return IntStream.of(array)
                .filter(i -> i >= min && i < max)
                .sorted()
                .toArray();
    }
}
