package edu.project4.nonlineartransformations.affinetransformations;

import edu.project4.renderers.plotters.Point;
import java.util.function.Function;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public final class AffineRotation {

    private AffineRotation() {

    }

    public static Function<Point, Point> getRotation(double radAngle) {
        return point -> {
            double x1 = point.x() * cos(radAngle) - point.y() * sin(radAngle);
            double y1 = point.x() * sin(radAngle) + point.y() * cos(radAngle);
            return new Point(x1, y1);
        };
    }

}
