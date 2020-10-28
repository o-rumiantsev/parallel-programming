package com.lab4;

import com.lab4.task1.*;
import com.lab4.task2.*;
import com.lab4.task3.*;
import com.lab4.task4.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        runTask4();
    }

    public static void runTask1() {
        Storage storage = new Storage(10);
        Producer p1 = new Producer(1, storage);
        Producer p2 = new Producer(2, storage);
        Consumer c1 = new Consumer(1, storage);
        Consumer c2 = new Consumer(2, storage);

        new Thread(p1).start();
        new Thread(p2).start();
        new Thread(c1).start();
        new Thread(c2).start();
    }

    public static void runTask2() {
        final int readersCount = 2;
        final int writersCount = 1;
        ExecutorService threadPool = Executors.newFixedThreadPool(
                readersCount + writersCount
        );

        for (int id = 1; id <= readersCount; ++id) {
            threadPool.execute(new Reader(id, Writer.writerSemaphore));
        }

        for (int id = 1; id <= writersCount; ++id) {
            threadPool.execute(new Writer(id));
        }
    }

    public static void runTask3() {
        final int philosophersCount = 5;

        ExecutorService threadPool = Executors.newFixedThreadPool(philosophersCount);
        Fork[] forks = new Fork[philosophersCount];

        for (int i = 0; i < forks.length; ++i) {
            forks[i] = new Fork();
        }

        for (int i = 0; i < philosophersCount; ++i) {
            Fork leftFork = forks[i];
            Fork rightFork = forks[(i + 1) % forks.length];
            Philosopher philosopher;

            if (i == philosophersCount - 1) {
                philosopher = new Philosopher(
                        "Philosopher " + (i + 1),
                        rightFork,
                        leftFork
                );
            } else {
                philosopher = new Philosopher(
                        "Philosopher " + (i + 1),
                        leftFork,
                        rightFork
                );
            }

            threadPool.execute(philosopher);
        }
    }

    public static void runTask4() {
        BarberShop barberShop = new BarberShop();
        Barber barber = new Barber(barberShop);
        barber.start();
        while (true) {
            new Thread(new Client(barberShop)).start();
            try {
                Thread.sleep((int) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
