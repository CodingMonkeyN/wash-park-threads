package org.example;

import java.util.concurrent.atomic.AtomicInteger;

public class Car implements Runnable {
    private static final AtomicInteger carCounter = new AtomicInteger(0);
    private final int carId;
    private final WashPark washPark;
    private final boolean interiorCleaningRequested;

    public Car(final int simulationTime, final WashPark washPark, final boolean interiorCleaningRequested) {
        this.carId = carCounter.incrementAndGet();
        this.washPark = washPark;
        this.interiorCleaningRequested = interiorCleaningRequested;
        if (this.interiorCleaningRequested) {
            System.out.println("Time: " + simulationTime + ": Car " + this.carId + " arrived at the wash part and wants to wash and clean its interior");
        } else {
            System.out.println("Time: " + simulationTime + ": Car " + this.carId + " arrived and wants to wash only");
        }
    }

    public int Id() {
        return this.carId;
    }

    @Override
    public void run() {
        if (this.interiorCleaningRequested) {
            try {
                this.washPark.washCar(this);
                this.washPark.cleanInterior(this);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                this.washPark.washCar(this);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Car " + this.carId + " finished cleaning and left the wash park");
    }
}
