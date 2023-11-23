package edu.hw7.task4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class MultiThreadMonteCarloApproximationCalc extends AbstractMonteCarloPiApproximationCalc {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String EXECUTION_TIME_MESSAGE_TEMPLATE = "%d threads: %d ms";
    private final static String ERROR_MESSAGE_TEMPLATE = "Error: %f";

    private volatile int totalCount;
    private volatile int circleCount;
    private int iterations;

    public MultiThreadMonteCarloApproximationCalc(int iterations) {
        this.iterations = iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    @Override
    public double calcPi() {
        int numOfThreads = Runtime.getRuntime().availableProcessors();
        int iterationsPerThread = iterations / numOfThreads;
        int threadsWithAdditionalIteration = iterations % numOfThreads;
        long start = System.currentTimeMillis();
        try (ExecutorService service = Executors.newFixedThreadPool(numOfThreads)) {
            for (int i = 0; i < numOfThreads; i++) {
                if (i < threadsWithAdditionalIteration) {
                    service.execute(getIterationTask(iterationsPerThread + 1));
                } else {
                    service.execute(getIterationTask(iterationsPerThread));
                }
            }
        } catch (RejectedExecutionException ignored) {

        }
        double res = FORMULA_MULTIPLIER * (double) circleCount / totalCount;
        long end = System.currentTimeMillis();
        LOGGER.info(String.format(EXECUTION_TIME_MESSAGE_TEMPLATE, numOfThreads, end - start));
        LOGGER.info(String.format(ERROR_MESSAGE_TEMPLATE, Math.abs(Math.PI - res) / Math.PI));
        return res;
    }

    private Runnable getIterationTask(int iterations) {
        return () -> {
            Point point;
            int total = 0;
            int circle = 0;
            for (int i = 0; i < iterations; i++) {
                point = getRandomPoint();
                if (point.isInCircle()) {
                    circle++;
                }
                total++;
            }
            accumulateCounts(total, circle);
        };
    }

    private synchronized void accumulateCounts(int totalCount, int circleCount) {
        this.totalCount += totalCount;
        this.circleCount += circleCount;
    }

}
