package com.lab4.task1;

public class Producer implements Runnable {
    private final int producerId;
    private final Storage storage;

    public Producer(int producerId, Storage storage) {
        this.producerId = producerId;
        this.storage = storage;
    }

    @Override
    public void run() {
        for (int i = 0;; i++) {
            Item item = new Item(
                    "Producer " +
                            producerId +
                            " message " +
                            (i + 1)
            );
            storage.put(producerId, item);
            try {
                Thread.sleep((int) (Math.random() * 1000));
            } catch (InterruptedException exception) {
                exception.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}