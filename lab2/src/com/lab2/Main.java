package com.lab2;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Config config = Config.getInstance();
        config.readRandomRangeBounds();

        CPUManager cpuManager = new CPUManager();
        for (int cpuId = 1; cpuId < 4; cpuId++) {
            CPU cpu = new CPU();
            cpuManager.addCpu(cpuId, cpu);
            new Thread(cpu).start();
        }

        ArrayList<Thread> streamThreads = new ArrayList<Thread>();
        ArrayList<CPUProcessStream> streams = new ArrayList<CPUProcessStream>();
        for (int streamId = 1; streamId < 4; streamId++) {
            int[] cpuIds = config.getCPUIds(streamId);
            int tasksCount = config.getTasksCountForStream(streamId);
            CPUProcessStream cpuProcessStream =
                    new CPUProcessStream(
                            streamId,
                            tasksCount,
                            cpuManager,
                            cpuIds
                    );
            streams.add(cpuProcessStream);
            streamThreads.add(new Thread(cpuProcessStream));
        }

        for (Thread streamThread: streamThreads) {
            streamThread.start();
        }

        for (Thread streamThread: streamThreads) {
            streamThread.join();
        }

        for (CPUProcessStream cpuProcessStream: streams) {
            cpuProcessStream.printStats();
        }
    }
}
