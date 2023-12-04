package edu.project4.renderers.plotters;

import edu.project4.nonlineartransformations.NonLinearTransformation;
import java.util.List;
import java.util.Random;

public class SingleThreadPlotter extends AbstractPlotter {

    public SingleThreadPlotter(
        int width,
        int height,
        int samples,
        int iterations,
        int affineTransformationCounts,
        List<String> nonLinearTransformationKeys,
        boolean withSymmetry
    ) {
        super(
            width,
            height,
            samples,
            iterations,
            affineTransformationCounts,
            nonLinearTransformationKeys,
            withSymmetry
        );
    }

    @Override
    protected void generate() {
        Random random = new Random();
        Point point;
        int rand;
        for (int n = 0; n < samples; n++) {
            point = new Point(random.nextDouble(xMin, xMax), random.nextDouble(yMin, yMax));
            for (int step = -20; step < iterations; step++) {
                rand = random.nextInt(variationsNum);
                if (rand >= transformations.size()) {
                    point = plotSymmetrical(point, step);
                } else {
                    point = plotNonLinear(point, step, rand);
                }
            }
        }
    }

    private Point plotSymmetrical(Point point, int iterationStep) {
        int curWidth = width - (int) (((xMax - point.x()) / (xMax - xMin)) * width);
        int curHeight = height - (int) (((yMax - point.y()) / (yMax - yMin)) * height);
        int red;
        int green;
        int blue;
        long hitCount;
        Point rotatedPoint;
        if (image.contains(curWidth, curHeight)) {
            red = image.pixel(curWidth, curHeight).r();
            green = image.pixel(curWidth, curHeight).g();
            blue = image.pixel(curWidth, curHeight).b();
            hitCount = image.pixel(curWidth, curHeight).hitCount();
            rotatedPoint = SYMMETRY.apply(point);
            if (iterationStep >= 0 && dim.contains(rotatedPoint)) {
                curWidth = width - (int) (((xMax - rotatedPoint.x()) / (xMax - xMin)) * width);
                curHeight = height - (int) (((yMax - rotatedPoint.y()) / (yMax - yMin)) * height);
                if (image.contains(curWidth, curHeight)) {
                    image.data()[curHeight][curWidth] = new Pixel(red, green, blue, ++hitCount);
                }
            }
            return rotatedPoint;
        } else {
            return point;
        }
    }

    private Point plotNonLinear(Point point, int iterationStep, int randIndex) {
        NonLinearTransformation transformation = transformations.get(randIndex);
        int curWidth;
        int curHeight;
        int red;
        int green;
        int blue;
        long hitCount;
        Point translatedPoint;
        translatedPoint = transformation.apply(point);
        if (iterationStep >= 0 && dim.contains(translatedPoint)) {
            curWidth = width - (int) (((xMax - translatedPoint.x()) / (xMax - xMin)) * width);
            curHeight = height - (int) (((yMax - translatedPoint.y()) / (yMax - yMin)) * height);
            if (image.contains(curWidth, curHeight)) {
                red = transformation.getRedChannel();
                green = transformation.getGreenChannel();
                blue = transformation.getBlueChannel();
                hitCount = image.pixel(curWidth, curHeight).hitCount();
                if (hitCount != 0) {
                    red = ((image.pixel(curWidth, curHeight).r() + red) / 2);
                    green = ((image.pixel(curWidth, curHeight).g() + green) / 2);
                    blue = ((image.pixel(curWidth, curHeight).b() + blue) / 2);
                }
                image.data()[curHeight][curWidth] = new Pixel(red, green, blue, ++hitCount);
            }
        }
        return translatedPoint;
    }

}
