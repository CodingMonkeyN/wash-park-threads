package org.example;


import java.util.ArrayList;
import java.util.Random;

public class Simulation {
    public static void main(String[] args) throws InterruptedException {
        var simulationDuration = 4 * 60;
        var simulation = new Simulation();
        var washPark = new WashPark();

        var threads = new ArrayList<Thread>();
        var finished = false;
        var i = 0;
        while (!finished) {
            washPark.setSimulationTime(i);
            var allFinished = (threads.isEmpty()) || threads.stream().map(Thread::isAlive).noneMatch(b -> b);
            if ((i > simulationDuration) && allFinished) {
                finished = true;
            }
            if (i < simulationDuration && (i % 5 == 0)) {
                var carCount = simulation.getCarsFor(i);
                var cars = new ArrayList<Car>();
                for (int j = 0; j < carCount; j++) {
                    cars.add(new Car(i, washPark, simulation.getInteriorWashingFor(i)));
                }
                cars.forEach(car -> {
                    var thread = new Thread(car);
                    threads.add(thread);
                    thread.start();
                });
            }
            Thread.sleep(1000);
            i++;
        }
        System.out.println("Simulation finished after " + i + " minutes");
    }

    public int getCarsFor(int minute) {
        if (minute < 60) {
            return new Random().nextInt(3) + 1;
        }
        if (minute < 120) {
            return new Random().nextInt(5) + 3;
        }

        return new Random().nextInt(2) + 1;
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
