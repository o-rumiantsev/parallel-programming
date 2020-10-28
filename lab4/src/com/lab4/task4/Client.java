package com.lab4.task4;

public class Client extends Thread {
    private final BarberShop barberShop;

    public Client(BarberShop barberShop) {
        this.barberShop = barberShop;
    }

    @Override
    public void run() {
        try {
            barberShop.seatsSemaphore.acquire();
            if (barberShop.freeSeatsCount > 0) {
                barberShop.freeSeatsCount--;
                System.out.println(
                        "Client occupied seat. Free seats " +
                                barberShop.freeSeatsCount
                );
                barberShop.clientsSemaphore.release();
                barberShop.seatsSemaphore.release();
                barberShop.barberSemaphore.acquire();
            } else {
                System.out.println("Client not found free seats");
                barberShop.seatsSemaphore.release();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}