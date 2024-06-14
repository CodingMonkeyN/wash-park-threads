package org.example;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WashPark {
    private final Lock washStreetLock = new ReentrantLock();
    private final Lock interiorCleaningBoxLock = new ReentrantLock();
    private final Condition washStreetCondition = this.washStreetLock.newCondition();
    private final Condition interiorCleaningBoxCondition = this.interiorCleaningBoxLock.newCondition();
    private int availableWashStreets = 3;
    private int availableInteriorCleaningBoxes = 2;
    private volatile int simulationTime;

    public void washCar(Car car) throws InterruptedException {
        this.washStreetLock.lock();
        try {
            while (this.availableWashStreets == 0) {
                this.washStreetCondition.await();
            }
            this.availableWashStreets--;

        } finally {
            this.washStreetLock.unlock();
        }
        var requiredDuration = new Random().nextInt(8) + 5;
        System.out.println("Time: " + this.simulationTime + ": Car " + car.Id() + " is using a wash street and needs " + requiredDuration + " minutes");
        Thread.sleep(requiredDuration * 1000);
        System.out.println("Time: " + this.simulationTime + ": Car " + car.Id() + " finished washing");

        this.washStreetLock.lock();
        try {
            this.availableWashStreets++;
            this.washStreetCondition.signal();
        } finally {
            this.washStreetLock.unlock();
        }
    }

    public void setSimulationTime(int simulationTime) {
        this.simulationTime = simulationTime;
    }

    public void cleanInterior(Car car) throws InterruptedException {
        this.interiorCleaningBoxLock.lock();
        try {
            while (this.availableInteriorCleaningBoxes == 0) {
                this.interiorCleaningBoxCondition.await();
            }
            this.availableInteriorCleaningBoxes--;

        } finally {
            this.interiorCleaningBoxLock.unlock();
        }
        var requiredDuration = (new Random().nextInt(3) + 1) * 5;
        System.out.println("Time: " + this.simulationTime + ": Car " + car.Id() + " is using an interior cleaning box and needs " + requiredDuration + " minutes");
        Thread.sleep(requiredDuration * 1000);
        System.out.println("Time: " + this.simulationTime + ": Car " + car.Id() + " finished interior cleaning");
        this.interiorCleaningBoxLock.lock();

        try {
            this.availableInteriorCleaningBoxes++;
        } finally {
            this.interiorCleaningBoxLock.unlock();
        }
    }
}
