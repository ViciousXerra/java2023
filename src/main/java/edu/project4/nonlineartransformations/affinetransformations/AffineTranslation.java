package edu.project4.nonlineartransformations.affinetransformations;

import edu.project4.Point;
import java.util.concurrent.ThreadLocalRandom;

public final class AffineTranslation extends AffineTransformation {

    private static final int BYTE_BOUND = 256;
    private final AffineTransformationCoefficients coefficients;
    private final short rChannel;
    private final short gChannel;
    private final short bChannel;

    private AffineTranslation() {
        coefficients = AffineTransformationCoefficientsGenerator.generate();
        rChannel = (short) ThreadLocalRandom.current().nextInt(0, BYTE_BOUND);
        gChannel = (short) ThreadLocalRandom.current().nextInt(0, BYTE_BOUND);
        bChannel = (short) ThreadLocalRandom.current().nextInt(0, BYTE_BOUND);
    }

    public static AffineTranslation getInstance() {
        return new AffineTranslation();
    }

    public AffineTransformationCoefficients getCoefficients() {
        return coefficients;
    }

    @Override
    public Point apply(Point point) {
        double x = coefficients.a() * point.x() + coefficients.b() * point.y() + coefficients.c();
        double y = coefficients.d() * point.x() + coefficients.e() * point.y() + coefficients.f();
        return new Point(x, y);
    }

    public short getRedChannel() {
        return rChannel;
    }

    public short getGreenChannel() {
        return gChannel;
    }

    public short getBlueChannel() {
        return bChannel;
    }

}
