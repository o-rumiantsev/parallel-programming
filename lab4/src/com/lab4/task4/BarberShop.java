package com.lab4.task4;

import java.util.concurrent.*;

public class BarberShop {
    public final int MAX_CLIENTS_COUNT = 10;
    public final Semaphore barberSemaphore = new Semaphore(1, true);
    public final Semaphore clientsSemaphore = new Semaphore(0, true);
    public final Semaphore seatsSemaphore = new Semaphore(1, true);
    public int freeSeatsCount = MAX_CLIENTS_COUNT;
}
