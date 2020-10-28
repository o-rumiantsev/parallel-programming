package com.lab4.task1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Storage {
    private final int capacity;
    private final List<Item> items;
    private final Lock locker;
    private final Condition notFull;
    private final Condition notEmpty;

    public Storage(int capacity) {
        this.capacity = capacity;
        this.items = new ArrayList<>();
        this.locker = new ReentrantLock();
        this.notFull = locker.newCondition();
        this.notEmpty = locker.newCondition();
    }

    public void put(int producerId, Item item) {
        locker.lock();
        try {
            while (items.size() == capacity) {
                System.out.println("Storage is full");
                notFull.await();
            }
            items.add(item);
            System.out.println("Producer " + producerId + " put " + item.getMessage());
            notEmpty.signalAll();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            locker.unlock();
        }
    }

    public void take(int consumerId) {
        locker.lock();
        try {
            while (items.size() <= 0) {
                System.out.println("Storage is empty");
                notEmpty.await();
            }
            Item item = items.remove(0);
            System.out.println("Consumer " + consumerId + " took " + item.getMessage());
            notFull.signalAll();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            locker.unlock();
        }
    }

}