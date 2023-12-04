package edu.project4.plotters;

import edu.project4.nonlineartransformations.NonLinearTransformation;
import edu.project4.nonlineartransformations.affinetransformations.AffineRotation;
import edu.project4.nonlineartransformations.affinetransformations.AffineTranslation;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract class AbstractPlotter implements Plotter {

    protected final static Function<Point, Point> SYMMETRY = AffineRotation.getRotation(Math.PI);
    protected final static int ADJUST_STEP = 20;
    protected final int width;
    protected final int height;
    protected final PixelsImage image;
    protected final double xMin;
    protected final double xMax;
    protected final double yMin;
    protected final double yMax;
    protected final Dimension dim;
    protected final int samples;
    protected final int iterations;
    protected final List<NonLinearTransformation> transformations;
    protected final int variationsNum;
    private final static Logger LOGGER = LogManager.getLogger();
    private final static double GAMMA_FACTOR = 2.2;

    protected AbstractPlotter(
        int width,
        int height,
        int samples,
        int iterations,
        int affineTransformationCounts,
        List<String> nonLinearTransformationKeys,
        boolean withSymmetry
    ) {
        validate(width, height, affineTransformationCounts, nonLinearTransformationKeys, samples, iterations);
        this.width = width;
        this.height = height;
        this.samples = samples;
        this.iterations = iterations;
        image = PixelsImage.create(width, height);
        xMin = -(double) width / height;
        xMax = -xMin;
        yMin = -1;
        yMax = 1;
        dim = new Dimension(2 * xMax, 2, xMin, yMin);
        transformations = getTransformationList(affineTransformationCounts, nonLinearTransformationKeys);
        variationsNum = withSymmetry ? transformations.size() * 2 : transformations.size();
    }

    @Override
    public final PixelsImage plotPixels() {
        long start = System.currentTimeMillis();
        generate();
        postProcess();
        LOGGER.info(String.format("Session time: %d ms", System.currentTimeMillis() - start));
        return image;
    }

    protected abstract void generate();

    private void postProcess() {
        double max = logarithmicCorrection();
        gammaCorrection(max);
    }

    private double logarithmicCorrection() {
        double max = 0;
        double normal;
        Pixel p;
        for (int curWidth = 0; curWidth < width; curWidth++) {
            for (int curHeight = 0; curHeight < height; curHeight++) {
                p = image.pixel(curWidth, curHeight);
                if (p.hitCount() != 0) {
                    normal = Math.log10(p.hitCount());
                    image.data()[curHeight][curWidth] = new Pixel(p.r(), p.g(), p.b(), p.hitCount(), normal);
                    if (normal > max) {
                        max = normal;
                    }
                }
            }
        }
        return max;
    }

    private void gammaCorrection(double max) {
        int red;
        int green;
        int blue;
        double normal;
        for (int curWidth = 0; curWidth < width; curWidth++) {
            for (int curHeight = 0; curHeight < height; curHeight++) {
                normal = image.pixel(curWidth, curHeight).normal() / max;
                red = (int) (image.pixel(curWidth, curHeight).r() * Math.pow(normal, 1 / GAMMA_FACTOR));
                green = (int) (image.pixel(curWidth, curHeight).g() * Math.pow(normal, 1 / GAMMA_FACTOR));
                blue = (int) (image.pixel(curWidth, curHeight).b() * Math.pow(normal, 1 / GAMMA_FACTOR));
                image.data()[curHeight][curWidth] = new Pixel(
                    red,
                    green,
                    blue,
                    image.pixel(curWidth, curHeight).hitCount(),
                    normal
                );
            }
        }
    }

    private static void validate(
        int width,
        int height,
        int affineTransformationCount,
        List<String> nonLinearKeys,
        int samples,
        int iterations
    ) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Image resolution values must be a positive nums.");
        }
        if (affineTransformationCount <= 1) {
            throw new IllegalArgumentException("Affine transformation count argument must be at least 2.");
        }
        if (nonLinearKeys == null || nonLinearKeys.isEmpty()) {
            throw new IllegalArgumentException("Non-linear transformation keys must exist.");
        }
        if (samples <= 0 || iterations <= 0) {
            throw new IllegalArgumentException("Num of samples and iterations must be at least 1.");
        }
    }

    private static List<NonLinearTransformation> getTransformationList(int affineTranslationCount, List<String> keys) {
        List<AffineTranslation> translations = new ArrayList<>(affineTranslationCount);
        for (int i = 0; i < affineTranslationCount; i++) {
            translations.add(AffineTranslation.getInstance());
        }
        return keys
            .stream()
            .flatMap(key -> {
                List<NonLinearTransformation> list = new ArrayList<>();
                translations.forEach(affineTranslation -> list.add(NonLinearTransformation.getInstance(
                            affineTranslation,
                            key
                        )
                    )
                );
                return list.stream();
            })
            .toList();
    }

}
