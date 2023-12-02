package edu.project4;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public final class AffineTransformationCoefficientsGenerator {

    private final static double LOW_RANGE = -1.0;
    private final static double HIGH_RANGE = 1.0;

    private AffineTransformationCoefficientsGenerator() {

    }

    public static AffineTransformationCoefficients generate() {
        double c = getCoeff();
        double f = getCoeff();
        double a;
        double d;
        double b;
        double e;
        double conditionValue;
        do {
            a = getCoeff();
            d = getCoeff(sqrt(1- pow(a, 2)));
            b = getCoeff();
            e = getCoeff(sqrt(1- pow(b, 2)));
            conditionValue =
                pow(a, 2) +
                pow(b, 2) +
                pow(d, 2) +
                pow(e, 2) -
                pow((a * e - b * d), 2);
        } while (conditionValue >= 1.0);
        return new AffineTransformationCoefficients(a, b, c, d, e, f);
    }

    private static double getCoeff() {
        return ThreadLocalRandom.current().nextDouble(LOW_RANGE, HIGH_RANGE);
    }

    private static double getCoeff(double bound) {
        return ThreadLocalRandom.current().nextDouble(0, bound);
    }

}
