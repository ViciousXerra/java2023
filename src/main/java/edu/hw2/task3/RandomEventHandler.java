package edu.hw2.task3;

import java.util.Random;

class RandomEventHandler {

    private final static Random RANDOM = new Random();
    private final double probability;

    RandomEventHandler(double probability) {
        this.probability = probability;
    }

    boolean getEventOutcome() {
        return RANDOM.nextDouble() >= probability;
    }

}
