package com.lab2;

import java.util.Scanner;

public class Config {
    private static Config instance = new Config();

    public static Config getInstance() {
        return instance;
    }

    private long lowerRandomRangeBound;
    private long upperRandomRangeBound;
    private final int[][] cpuProcessStreamCpus = new int[][] {
            {1},
            {1, 2},
            {1, 3}
    };

    public long getLowerRandomRangeBound() {
        return lowerRandomRangeBound;
    }

    public long getUpperRandomRangeBound() {
        return upperRandomRangeBound;
    }

    public int[] getCPUIds(int streamId) {
        return cpuProcessStreamCpus[streamId - 1];
    }

    public int getTasksCountForStream(int streamId) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter tasks count for stream " + streamId + ": ");
        return scanner.nextInt();
    }

    public void readRandomRangeBounds() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter lower random range bound: ");
        lowerRandomRangeBound = scanner.nextLong();

        System.out.print("Enter upper random range bound: ");
        upperRandomRangeBound = scanner.nextLong();
    }
}
