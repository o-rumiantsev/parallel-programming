package com.lab4.task3;

public class Philosopher implements Runnable {
    private final String name;
    private final Fork leftFork;
    private final Fork rightFork;

    public Philosopher(String name, Fork leftFork, Fork rightFork) {
        this.name = name;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    @Override
    public void run() {
        try {
            while (true) {
                act(System.nanoTime() + ": Thinking");
                synchronized (leftFork) {
                    act(System.nanoTime() + ": Picked up left fork");
                    synchronized (rightFork) {
                        act(System.nanoTime() + ": Picked up right fork - eating");
                        act(System.nanoTime() + ": Put down right fork");
                    }
                    act(System.nanoTime() + ": Put down left fork. Back to thinking");
                }
            }

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }

    public void act(String action) throws InterruptedException {
        System.out.println(name + " " + action);
        Thread.sleep(((int) (Math.random() * 1000 + 100)));
    }
}
