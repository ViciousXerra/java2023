package edu.project4.nonlineartransformations;

import edu.project4.Point;
import edu.project4.nonlineartransformations.affinetransformations.AffineTransformationCoefficients;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

final class NonLinearTransformationUtils {

    private final static BiFunction<Point, AffineTransformationCoefficients, Point> LINEAR =
        (point, coefficients) -> new Point(point.x(), point.y());
    private final static BiFunction<Point, AffineTransformationCoefficients, Point> SINUSOIDAL =
        (point, coefficients) -> new Point(sin(point.x()), sin(point.y()));
    private final static BiFunction<Point, AffineTransformationCoefficients, Point> SPHERICAL =
        (point, coefficients) -> {
            double rSquare = getRSquare(point.x(), point.y());
            return new Point(point.x() / rSquare, point.y() / rSquare);
        };
    private final static BiFunction<Point, AffineTransformationCoefficients, Point> SWIRL =
        (point, coefficients) -> {
            double rSquare = getRSquare(point.x(), point.y());
            return new Point(
                point.x() * sin(rSquare) - point.y() * cos(rSquare),
                point.x() * cos(rSquare) + point.y() * sin(rSquare)
            );
        };
    private final static BiFunction<Point, AffineTransformationCoefficients, Point> HORSESHOE =
        (point, coefficients) -> {
            double r = sqrt(getRSquare(point.x(), point.y()));
            return new Point(
                (point.x() - point.y()) * (point.x() + point.y()) / r,
                (2 * point.x() * point.y()) / r
            );
        };
    private final static BiFunction<Point, AffineTransformationCoefficients, Point> HANDKERCHIEF =
        (point, coefficients) -> {
            double r = sqrt(getRSquare(point.x(), point.y()));
            double theta = getTheta(point.x(), point.y());
            return new Point(
                r * sin(theta + r),
                r * cos(theta - r)
            );
        };
    private final static BiFunction<Point, AffineTransformationCoefficients, Point> HEART =
        (point, coefficients) -> {
            double r = sqrt(getRSquare(point.x(), point.y()));
            double theta = getTheta(point.x(), point.y());
            return new Point(
                r * sin(theta * r),
                r * cos(theta * r) * -1
            );
        };
    private final static BiFunction<Point, AffineTransformationCoefficients, Point> DISC =
        (point, coefficients) -> {
            double r = sqrt(getRSquare(point.x(), point.y()));
            double theta = getTheta(point.x(), point.y());
            double multiplier = theta / PI;
            return new Point(
                multiplier * sin(PI * r),
                multiplier * cos(PI * r)
            );
        };
    private final static BiFunction<Point, AffineTransformationCoefficients, Point> SPIRAL =
        (point, coefficients) -> {
            double r = sqrt(getRSquare(point.x(), point.y()));
            double theta = getTheta(point.x(), point.y());
            return new Point(
                (cos(theta) + sin(r)) / r,
                (sin(theta) - cos(r)) / r
            );
        };
    private final static BiFunction<Point, AffineTransformationCoefficients, Point> HYPERBOLIC =
        (point, coefficients) -> {
            double r = sqrt(getRSquare(point.x(), point.y()));
            double theta = getTheta(point.x(), point.y());
            return new Point(
                sin(theta) / r,
                r * cos(theta)
            );
        };
    private final static BiFunction<Point, AffineTransformationCoefficients, Point> DIAMOND =
        (point, coefficients) -> {
            double r = sqrt(getRSquare(point.x(), point.y()));
            double theta = getTheta(point.x(), point.y());
            return new Point(
                sin(theta) * cos(r),
                cos(theta) * sin(r)
            );
        };
    private final static BiFunction<Point, AffineTransformationCoefficients, Point> EX =
        (point, coefficients) -> {
            double r = sqrt(getRSquare(point.x(), point.y()));
            double theta = getTheta(point.x(), point.y());
            double p0 = sin(theta + r);
            double p1 = cos(theta - r);
            return new Point(
                (pow(p0, 3) + pow(p1, 3)) * r,
                (pow(p0, 3) - pow(p1, 3)) * r
            );
        };
    private final static BiFunction<Point, AffineTransformationCoefficients, Point> BENT =
        (point, coefficients) -> {
            if (point.x() >= 0 && point.y() >= 0) {
                return new Point(point.x(), point.y());
            } else if (point.x() < 0 && point.y() >= 0) {
                return new Point(2 * point.x(), point.y());
            } else if (point.x() >= 0 && point.y() < 0) {
                return new Point(point.x(), point.y() / 2);
            } else {
                return new Point(2 * point.x(), point.y() / 2);
            }
        };
    private final static BiFunction<Point, AffineTransformationCoefficients, Point> WAVES =
        (point, coefficients) -> {
            double x1 = point.x() + coefficients.b() * sin(point.y() / pow(coefficients.c(), 2));
            double y1 = point.y() + coefficients.e() * sin(point.x() / pow(coefficients.f(), 2));
            return new Point(x1, y1);
        };
    private final static BiFunction<Point, AffineTransformationCoefficients, Point> POPCORN =
        (point, coefficients) -> {
            double x1 = point.x() + coefficients.c() * sin(tan(3 * point.y()));
            double y1 = point.y() + coefficients.f() * sin(tan(3 * point.x()));
            return new Point(x1, y1);
        };
    private final static BiFunction<Point, AffineTransformationCoefficients, Point> EXPONENTIAL =
        (point, coefficients) -> {
            double exp = getExp(point.x());
            return new Point(
                exp * cos(PI * point.y()),
                exp * sin(PI * point.y())
            );
        };
    private final static BiFunction<Point, AffineTransformationCoefficients, Point> TANGENT =
        (point, coefficients) -> new Point(
            sin(point.x()) / cos(point.y()),
            tan(point.y())
        );
    private final static BiFunction<Point, AffineTransformationCoefficients, Point> CROSS =
        (point, coefficients) -> {
            double multiplier = sqrt(1 / pow((pow(point.x(), 2) - pow(point.y(), 2)), 2));
            return new Point(
                multiplier * point.x(),
                multiplier * point.y()
            );
        };

    private final static Map<String, BiFunction<Point, AffineTransformationCoefficients, Point>>
        TRANSFORMATION_MAPPING = new HashMap<>() {
        {
            put("sinusoidal", SINUSOIDAL);
            put("spherical", SPHERICAL);
            put("swirl", SWIRL);
            put("horseshoe", HORSESHOE);
            put("handkerchief", HANDKERCHIEF);
            put("heart", HEART);
            put("disc", DISC);
            put("spiral", SPIRAL);
            put("hyperbolic", HYPERBOLIC);
            put("diamond", DIAMOND);
            put("ex", EX);
            put("bent", BENT);
            put("waves", WAVES);
            put("popcorn", POPCORN);
            put("exponential", EXPONENTIAL);
            put("tangent", TANGENT);
            put("cross", CROSS);
        }
    };

    private NonLinearTransformationUtils() {

    }

    public static BiFunction<Point, AffineTransformationCoefficients, Point> getFunc(String key) {
        return TRANSFORMATION_MAPPING.getOrDefault(key, LINEAR);
    }

    private static double getRSquare(double x, double y) {
        return pow(x, 2) + pow(y, 2);
    }

    private static double getTheta(double x, double y) {
        return atan(x / y);
    }

    private static double getExp(double x) {
        return exp(x - 1);
    }

}
