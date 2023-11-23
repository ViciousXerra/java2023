package edu.hw7;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task1Test {

    private final static Logger LOGGER = LogManager.getLogger();

    private static Object[][] provideTestMethodWithData() {
        return new Object[][] {
            {7, 7, 0},
            {20, 8, 12},
            {0, 5, -5},
            {16, 32, -16},
        };
    }

    @ParameterizedTest
    @DisplayName("Test written class.")
    @MethodSource("provideTestMethodWithData")
    void testClassMethod(long expected, int countDown, long initialValue) {
        //When
        Task1.MultiThreadCounter counter = new Task1.MultiThreadCounter(initialValue);
        CountDownLatch latch = new CountDownLatch(countDown);
        try (ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            for (int i = 0; i < countDown; i++) {
                service.execute(() -> {
                    counter.increment();
                    latch.countDown();
                });
            }
            latch.await();
        } catch (InterruptedException e) {
            LOGGER.error("Thread has been interrupted.");
        }
        long actual = counter.getResult();
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("Test static method.")
    @CsvSource(value = {
        "10, 5, 5",
        "8, -16, 24"
    })
    void testMethod(long expected, long initialValue, int numOfThreads) {
        //When
        long actual = Task1.countWithMultipleThreads(numOfThreads, initialValue);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("Test static method with restricted number of threads.")
    @ValueSource(ints = {0, -1, Integer.MIN_VALUE})
    void testThreadsNumRestriction(int numOfThreads) {
        assertThatThrownBy(() -> Task1.countWithMultipleThreads(numOfThreads, 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Number of threads can't be less than 1.");
    }
}
