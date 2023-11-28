package edu.hw7.task4;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class SingleThreadMonteCarloApproximationCalc extends AbstractMonteCarloPiApproximationCalc {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String EXECUTION_TIME_MESSAGE_TEMPLATE = "Single thread: %d ms";

    private int totalCount;
    private int circleCount;

    public SingleThreadMonteCarloApproximationCalc(int iterations) {
        super(iterations);
    }

    @Override
    public double calcPi() {
        Point p;
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            p = getRandomPoint();
            if (p.isInCircle()) {
                circleCount++;
            }
            totalCount++;
        }
        double res = FORMULA_MULTIPLIER * (double) circleCount / totalCount;
        long end = System.currentTimeMillis();
        LOGGER.info(String.format(EXECUTION_TIME_MESSAGE_TEMPLATE, end - start));
        LOGGER.info(String.format(ERROR_MESSAGE_TEMPLATE, Math.abs(Math.PI - res) / Math.PI));
        return res;
    }

}
