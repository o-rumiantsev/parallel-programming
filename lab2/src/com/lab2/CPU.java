package com.lab2;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class CPU implements Runnable {
    private final ReentrantLock locker = new ReentrantLock();
    private final LinkedBlockingQueue<CPUProcess> taskBus =
            new LinkedBlockingQueue<CPUProcess>();

    public ReentrantLock getLocker() {
        return locker;
    }

    public LinkedBlockingQueue<CPUProcess> getTaskBus() {
        return taskBus;
    }

    private void runTask(CPUProcess task) {
        locker.lock();
        try {
            Thread.sleep(task.getExecutionTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            locker.unlock();
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                CPUProcess task = taskBus.take();
                runTask(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
