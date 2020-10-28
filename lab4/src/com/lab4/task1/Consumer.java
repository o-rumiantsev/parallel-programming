package com.lab4.task1;

public class Consumer implements Runnable {
    private final int consumerId;
    private final Storage storage;

    public Consumer(int consumerId, Storage storage) {
        this.consumerId = consumerId;
        this.storage = storage;
    }

    @Override
    public void run() {
        while (true) {
            storage.take(consumerId);
            try {
                Thread.sleep((int) (Math.random() * 1000));
            } catch (InterruptedException exception) {
                exception.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}