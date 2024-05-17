package org.example;

import java.util.Random;

public class WashPark {
    private final Object washStreetLock = new Object();
    private final Object interiorCleaningBoxLock = new Object();
    private int availableWashStreets = 3;
    private int availableInteriorCleaningBoxes = 2;
    private volatile int simulationTime;

    public void washCar(Car car) throws InterruptedException {
        synchronized (this.washStreetLock) {
            while (this.availableWashStreets == 0) {
                this.washStreetLock.wait();
            }
            this.availableWashStreets--;
        }

        var requiredDuration = new Random().nextInt(8) + 5;
        System.out.println("Time: " + this.simulationTime + ": Car " + car.Id() + " is using a wash street and needs " + requiredDuration + " minutes");
        Thread.sleep(requiredDuration * 1000);
        System.out.println("Time: " + this.simulationTime + ": Car " + car.Id() + " finished washing");

        synchronized (this.washStreetLock) {
            this.availableWashStreets++;
            this.washStreetLock.notifyAll();
        }
    }

    public void setSimulationTime(int simulationTime) {
        this.simulationTime = simulationTime;
    }

    public void cleanInterior(Car car) throws InterruptedException {
        synchronized (this.interiorCleaningBoxLock) {
            while (this.availableInteriorCleaningBoxes == 0) {
                this.interiorCleaningBoxLock.wait();
            }
            this.availableInteriorCleaningBoxes--;
        }

        var requiredDuration = (new Random().nextInt(3) + 1) * 5;
        System.out.println("Time: " + this.simulationTime + ": Car " + car.Id() + " is using an interior cleaning box and needs " + requiredDuration + " minutes");
        Thread.sleep(requiredDuration * 1000);
        System.out.println("Time: " + this.simulationTime + ": Car " + car.Id() + " finished interior cleaning");

        synchronized (this.interiorCleaningBoxLock) {
            this.availableInteriorCleaningBoxes++;
            this.interiorCleaningBoxLock.notify();
        }
    }
}
