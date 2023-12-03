package edu.project4.renderers.plotters;

import edu.project4.nonlineartransformations.NonLinearTransformation;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultiThreadPlotter extends AbstractPlotter {

    private final static int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    private final Lock lock = new ReentrantLock();

    public MultiThreadPlotter(
        int width,
        int height,
        int samples,
        int iterations,
        long seed,
        int affineTransformationCounts,
        List<String> nonLinearTransformationKeys,
        boolean withSymmetry
    ) {
        super(
            width,
            height,
            samples,
            iterations,
            seed,
            affineTransformationCounts,
            nonLinearTransformationKeys,
            withSymmetry
        );
    }

    @Override
    protected void generate() {
        try (ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT)) {
            List<Runnable> tasks = getPlottingTasksList();
            tasks.forEach(service::execute);
            service.shutdown();
            while (!service.isTerminated()) {

            }
        }
    }

    private List<Runnable> getPlottingTasksList() {
        List<Runnable> tasks = new ArrayList<>();
        if (samples <= THREAD_COUNT) {
            for (int i = 0; i < samples; i++) {
                tasks.add(new PlottingTask(1));
            }
        } else {
            int samplesPerThread = samples / THREAD_COUNT;
            int threadWithAdditionalSample = samples % THREAD_COUNT;
            for (int i = 0; i < THREAD_COUNT; i++) {
                if (i < threadWithAdditionalSample) {
                    tasks.add(new PlottingTask(samplesPerThread + 1));
                    continue;
                }
                tasks.add(new PlottingTask(samplesPerThread));
            }
        }
        return tasks;
    }

    private class PlottingTask implements Runnable {

        private final int samples;

        private PlottingTask(int samples) {
            this.samples = samples;
        }

        @Override
        public void run() {
            Random random = ThreadLocalRandom.current();
            Point point;
            int rand;
            point = new Point(random.nextDouble(xMin, xMax), random.nextDouble(yMin, yMax));
            for (int n = 0; n < samples; n++) {
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
                    lock.lock();
                    try {
                        image.data()[curHeight][curWidth] = new Pixel(red, green, blue, ++hitCount);
                    } finally {
                        lock.unlock();
                    }
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
                lock.lock();
                try {
                    image.data()[curHeight][curWidth] = new Pixel(red, green, blue, ++hitCount);
                } finally {
                    lock.unlock();
                }
            }
        }
        return translatedPoint;
    }

}
