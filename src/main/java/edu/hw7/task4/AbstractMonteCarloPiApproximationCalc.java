package edu.hw7.task4;

import java.util.concurrent.ThreadLocalRandom;
import static java.lang.Math.pow;

abstract class AbstractMonteCarloPiApproximationCalc implements PiCalculator {

    private final static double SQUARE_SIZE = 2.0;
    private final static double CENTER_COORDINATE = 1.0;
    private final static double CIRCLE_RADIUS = 1.0;
    final static int FORMULA_MULTIPLIER = 4;

    static Point getRandomPoint() {
        return new Point(getRandomCoordinate(), getRandomCoordinate());
    }

    private static double getRandomCoordinate() {
        return ThreadLocalRandom.current().nextDouble(SQUARE_SIZE);
    }

    record Point(double x, double y) {

        boolean isInCircle() {
            return getDistancePow() <= pow(CIRCLE_RADIUS, 2);
        }

        private double getDistancePow() {
            return pow(x - CENTER_COORDINATE, 2) + pow(y - CENTER_COORDINATE, 2);
        }
    }

}
