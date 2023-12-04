package edu.project4.nonlineartransformations;

import edu.project4.nonlineartransformations.affinetransformations.AffineTransformationCoefficients;
import edu.project4.nonlineartransformations.affinetransformations.AffineTranslation;
import edu.project4.plotters.Point;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class NonLinearTransformation implements Function<Point, Point> {

    private final AffineTranslation translation;
    private final BiFunction<Point, AffineTransformationCoefficients, Point> biFunction;

    private NonLinearTransformation(
        AffineTranslation translation,
        BiFunction<Point, AffineTransformationCoefficients, Point> biFunction
    ) {
        this.translation = translation;
        this.biFunction = biFunction;
    }

    public static NonLinearTransformation getInstance(AffineTranslation translation, String key) {
        if (translation == null || key == null) {
            throw new IllegalArgumentException("Translation function and key must be non-null.");
        }
        return new NonLinearTransformation(translation, NonLinearTransformationUtils.getFunc(key));
    }

    @Override
    public Point apply(Point point) {
        return biFunction.apply(translation.apply(point), translation.getCoefficients());
    }

    public short getRedChannel() {
        return translation.getRedChannel();
    }

    public short getGreenChannel() {
        return translation.getGreenChannel();
    }

    public short getBlueChannel() {
        return translation.getBlueChannel();
    }

}
