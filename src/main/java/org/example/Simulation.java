package org.example;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Simulation {
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        PrintStream fileOut = new PrintStream(new File("output/simulation-run-cached-thread-pool.txt"));
        System.setOut(fileOut);
        var simulationDuration = 4 * 60;
        var simulation = new Simulation();
        var washPark = new WashPark();

        for (int i = 0; i < simulationDuration; i++) {
            washPark.setSimulationTime(i);
            if (i % 5 == 0) {
                var carCount = simulation.getCarsFor(i);
                var cars = new ArrayList<Car>();
                System.out.println("Time: " + i + ": " + carCount + " cars arrived");
                for (int j = 0; j < carCount; j++) {
                    cars.add(new Car(i, washPark, simulation.getInteriorWashingFor(i)));
                }
                cars.forEach(pool::submit);
            }
            Thread.sleep(1000);
        }
        pool.shutdown();
    }

    public int getCarsFor(int minute) {
        if (minute < 60) {
            return new Random().nextInt(1, 4);
        }
        if (minute < 120) {
            return new Random().nextInt(3, 6);
        }

        return new Random().nextInt(1, 3);
    }

    public boolean getInteriorWashingFor(int minute) {
        if (minute < 60) {
            return new Random().nextBoolean();
        }
        if (minute < 120) {
            return new Random().nextInt(3) == 0;
        }

        return true;
    }
}
