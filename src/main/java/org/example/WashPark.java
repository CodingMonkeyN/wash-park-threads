package org.example;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class WashPark {
    private final Semaphore washStreets = new Semaphore(3);
    private final Semaphore interiorCleaningBox = new Semaphore(2);
    private volatile int simulationTime;

    public void washCar(Car car) throws InterruptedException {
        this.washStreets.acquire();
        var requiredDuration = new Random().nextInt(8) + 5;
        System.out.println("Time: " + this.simulationTime + ": Car " + car.Id() + " is using a wash street and needs " + requiredDuration + " minutes");
        Thread.sleep(requiredDuration * 1000);
        System.out.println("Time: " + this.simulationTime + ": Car " + car.Id() + " finished washing");
        this.washStreets.release();
    }

    public void setSimulationTime(int simulationTime) {
        this.simulationTime = simulationTime;
    }

    public void cleanInterior(Car car) throws InterruptedException {
        this.interiorCleaningBox.acquire();
        var requiredDuration = (new Random().nextInt(3) + 1) * 5;
        System.out.println("Time: " + this.simulationTime + ": Car " + car.Id() + " is using an interior cleaning box and needs " + requiredDuration + " minutes");
        Thread.sleep(requiredDuration * 1000);
        System.out.println("Time: " + this.simulationTime + ": Car " + car.Id() + " finished interior cleaning");
        this.interiorCleaningBox.release();
    }
}
