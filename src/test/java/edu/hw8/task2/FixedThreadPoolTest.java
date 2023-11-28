package edu.hw8.task2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FixedThreadPoolTest {

    @ParameterizedTest
    @DisplayName("Testing non-positive number of threads.")
    @ValueSource(ints = {0, Integer.MIN_VALUE})
    void testNonPositiveThreadsNum(int num) {
        assertThatThrownBy(() -> {
            try (ThreadPool pool = FixedThreadPool.create(num)) {

            }
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Number of threads must be a positive num.");
    }

    @Test
    @DisplayName("Testing null Runnable.")
    void testNullRunnable() {
        assertThatThrownBy(() -> {
            try (ThreadPool threadPool = FixedThreadPool.create(4)) {
                threadPool.execute(null);
            }
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Runnable can't be null.");
    }

}
