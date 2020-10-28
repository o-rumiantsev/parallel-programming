package com.lab4.task2;

import java.util.concurrent.Semaphore;

public class Reader extends Thread {
    private static final Semaphore readersCountSemaphore = new Semaphore(1, true);
    private static int readersCount = 0;

    private final int id;
    private final Semaphore writerSemaphore;

    public Reader(int id, Semaphore writerSemaphore) {
        this.id = id;
        this.writerSemaphore = writerSemaphore;
    }

    private void startReading() throws InterruptedException {
        readersCountSemaphore.acquire();
        ++readersCount;

        if (readersCount == 1) {
            writerSemaphore.acquire();
        }
        System.out.println("Reader " + id + " start reading");
        System.out.println("Readers count " + readersCount);
        readersCountSemaphore.release();
    }

    private void endReading() throws InterruptedException {
        readersCountSemaphore.acquire();
        --readersCount;

        if (readersCount == 0) {
            writerSemaphore.release();
        }
        System.out.println("Reader " + id + " finish reading");
        System.out.println("Readers count " + readersCount);
        readersCountSemaphore.release();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep((int) (Math.random() * 100 + 1000));

                startReading();
                Thread.sleep((int) (Math.random() * 100 + 1000));
                endReading();
            }
        } catch (InterruptedException exception) {
            exception.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
