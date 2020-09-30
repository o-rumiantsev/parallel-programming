package com.lab2;

public class CPUProcessStream implements Runnable {
    private final int streamId;
    private final int tasksCount;
    private final CPUManager cpuManager;
    private int rejectedTasksCount = 0;
    private final int[] cpuIds;

    public CPUProcessStream(int streamId, int tasksCount, CPUManager cpuManager, int[] cpuIds) {
        this.streamId = streamId;
        this.tasksCount = tasksCount;
        this.cpuManager = cpuManager;
        this.cpuIds = cpuIds;
    }

    public void printStats() {
        System.out.println(
                "Stream " +
                        streamId +
                        " rejected tasks percent: " +
                        (float) rejectedTasksCount / (float) tasksCount
        );
    }

    @Override
    public void run() {
        Config config = Config.getInstance();

        long min = config.getLowerRandomRangeBound();
        long max = config.getUpperRandomRangeBound();

        int tasksLeft = this.tasksCount;
        while (tasksLeft-- > 0) {
            long executionTime = (long) (Math.random() * (max - min)) + min;
            CPUProcess task = new CPUProcess(executionTime);

            for (int cpuId: cpuIds) {
                System.out.println(
                        "Putting task " +
                                task.getId() +
                                " to cpu " +
                                cpuId
                );
                boolean taskAccepted = cpuManager.putTask(cpuId, task);
                if (taskAccepted) {
                    task.accept();
                    break;
                } else {
                    System.out.println(
                            "Task " +
                                    task.getId() +
                                    " rejected by cpu " +
                                    cpuId
                    );

                }
            }

            if (task.getIsRejected()) {
                System.out.println("Task rejected " + task.getId());
                rejectedTasksCount++;
            }

            if (tasksLeft != 0) {
                long duration = (long) (Math.random() * (max - min)) + min;
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
