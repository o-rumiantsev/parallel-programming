package com.lab4.task4;

public class Barber extends Thread {
    private final BarberShop barberShop;

    public Barber(BarberShop barberShop) {
        this.barberShop = barberShop;
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("Barber fell asleep");
                barberShop.clientsSemaphore.acquire();
                System.out.println("Barber woke up");
                barberShop.seatsSemaphore.acquire();
                barberShop.freeSeatsCount++;
                System.out.println("Free seats " + barberShop.freeSeatsCount);
                barberShop.seatsSemaphore.release();

                System.out.println("Barber is doing a haircut");
                Thread.sleep((int) (Math.random() * 100 + 1000));
                System.out.println("Barber finished doing a haircut");

                System.out.println("Barber released");
                barberShop.barberSemaphore.release();
            }
        } catch (InterruptedException exception) {
            exception.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}