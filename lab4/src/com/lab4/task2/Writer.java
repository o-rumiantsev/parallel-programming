package com.lab4.task2;

import java.util.concurrent.Semaphore;

public class Writer extends Thread {
    public static Semaphore writerSemaphore = new Semaphore(1, true);
    private final int id;

    public Writer(int id) {
        this.id = id;
    }

    private void startWriting() throws InterruptedException {
        writerSemaphore.acquire();
        System.out.println("Writer " + id + " start writing");
    }

    private void endWriting() {
        System.out.println("Writer " + id + " finish writing");
        writerSemaphore.release();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep((int) (Math.random() * 100 + 1000));

                startWriting();
                Thread.sleep((int) (Math.random() * 100 + 1000));
                endWriting();
            }
        } catch (InterruptedException exception) {
            exception.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
