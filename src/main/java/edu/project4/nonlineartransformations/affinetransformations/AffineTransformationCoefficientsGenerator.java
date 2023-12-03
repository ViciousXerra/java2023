package edu.project4.nonlineartransformations.affinetransformations;

import java.util.concurrent.ThreadLocalRandom;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public final class AffineTransformationCoefficientsGenerator {

    private final static double LOW_RANGE = -1.0;
    private final static double HIGH_RANGE = 1.0;

    private AffineTransformationCoefficientsGenerator() {

    }

    public static AffineTransformationCoefficients generate() {
        double c;
        double f;
        do {
            c = getCoeff();
        } while (c == 0);
        do {
            f = getCoeff();
        } while (f == 0);
        double a;
        double d;
        double b;
        double e;
        double conditionValue1;
        do {
            a = getCoeff();
            d = getCoeff(sqrt(1- pow(a, 2)));
            b = getCoeff();
            e = getCoeff(sqrt(1- pow(b, 2)));
            conditionValue1 =
                pow(a, 2) +
                pow(b, 2) +
                pow(d, 2) +
                pow(e, 2) -
                pow((a * e - b * d), 2);
        } while (conditionValue1 >= 1.0);
        return new AffineTransformationCoefficients(a, b, c, d, e, f);
    }

    private static double getCoeff() {
        return ThreadLocalRandom.current().nextDouble(LOW_RANGE, HIGH_RANGE);
    }

    private static double getCoeff(double bound) {
        return ThreadLocalRandom.current().nextDouble(-bound, bound);
    }

}
