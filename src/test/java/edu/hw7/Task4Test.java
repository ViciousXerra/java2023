package edu.hw7;

import edu.hw7.task4.MultiThreadMonteCarloApproximationCalc;
import edu.hw7.task4.SingleThreadMonteCarloApproximationCalc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task4Test {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String NEGATIVE_ITERATIONS_MESSAGE = "Number of iterations must be positive number.";
    @Test
    @DisplayName("Test invalid number of iterations.")
    void testNonPositiveNumOfIterations() {
        assertThatThrownBy(() -> new SingleThreadMonteCarloApproximationCalc(0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NEGATIVE_ITERATIONS_MESSAGE);

        assertThatThrownBy(() -> new MultiThreadMonteCarloApproximationCalc(1000).setIterations(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NEGATIVE_ITERATIONS_MESSAGE);
    }

    @Test
    @DisplayName("Demo single thread Monte-Carlo Pi approximation run.")
    void testSingleThread() {
        assertThatCode(() -> {
            SingleThreadMonteCarloApproximationCalc calculator
                = new SingleThreadMonteCarloApproximationCalc(1000000000);
            LOGGER.info(calculator.calcPi());
        })
            .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("Demo multi-thread Monte-Carlo Pi approximation run.")
    void testMultiThread() {
        assertThatCode(() -> {
            MultiThreadMonteCarloApproximationCalc calculator
                = new MultiThreadMonteCarloApproximationCalc(1000000000);
            LOGGER.info(calculator.calcPi());
        })
            .doesNotThrowAnyException();

    }

}
