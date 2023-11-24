package edu.hw7.task4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class MultiThreadMonteCarloApproximationCalc extends AbstractMonteCarloPiApproximationCalc {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String CAUGHT_EXCEPTION_MESSAGE = "Caught exception: %s";
    private final static String EXECUTION_TIME_MESSAGE_TEMPLATE = "%d threads: %d ms";

    private int totalCount;
    private int circleCount;

    public MultiThreadMonteCarloApproximationCalc(int iterations) {
        super(iterations);
    }

    @Override
    public double calcPi() {
        return calcPi(Runtime.getRuntime().availableProcessors());
    }

    public double calcPi(int numOfThreads) {
        List<Callable<IterationResult>> tasks = getTasksList(numOfThreads);
        List<Future<IterationResult>> results = new ArrayList<>();
        long start = System.currentTimeMillis();
        try (ExecutorService service = Executors.newFixedThreadPool(numOfThreads)) {
            results = service.invokeAll(tasks);
        } catch (InterruptedException e) {
            LOGGER.error(String.format(CAUGHT_EXCEPTION_MESSAGE, e.getMessage()));
        } catch (RejectedExecutionException ignored) {

        }
        results.forEach(future -> {
            try {
                IterationResult result = future.get();
                accumulateCounts(result.circleCount, result.totalCount);
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error(String.format(CAUGHT_EXCEPTION_MESSAGE, e.getMessage()));
            }
        });
        double res = FORMULA_MULTIPLIER * (double) circleCount / totalCount;
        long end = System.currentTimeMillis();
        LOGGER.info(String.format(EXECUTION_TIME_MESSAGE_TEMPLATE, numOfThreads, end - start));
        if (totalCount < iterations) {
            LOGGER.info(String.format("Warning. Actual number of iterations was %d", totalCount));
        }
        LOGGER.info(String.format(ERROR_MESSAGE_TEMPLATE, Math.abs(Math.PI - res) / Math.PI));
        return res;
    }

    private List<Callable<IterationResult>> getTasksList(int numOfThreads) {
        int iterationsPerThread = iterations / numOfThreads;
        int threadsWithAdditionalIteration = iterations % numOfThreads;
        List<Callable<IterationResult>> tasks = new ArrayList<>();
        for (int i = 0; i < numOfThreads; i++) {
            if (i < threadsWithAdditionalIteration) {
                tasks.add(getIterationTask(iterationsPerThread + 1));
            } else {
                tasks.add(getIterationTask(iterationsPerThread));
            }
        }
        return tasks;
    }

    private Callable<IterationResult> getIterationTask(int iterations) {
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
            return new IterationResult(circle, total);
        };
    }

    private void accumulateCounts(int circleCount, int totalCount) {
        this.circleCount += circleCount;
        this.totalCount += totalCount;
    }

    private record IterationResult(int circleCount, int totalCount) {
    }

}
