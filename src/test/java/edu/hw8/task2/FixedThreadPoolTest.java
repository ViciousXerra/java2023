package edu.hw8.task2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FixedThreadPoolTest {

    private final static Logger LOGGER = LogManager.getLogger();

    private static class Task implements Runnable {

        private final int num;
        private long fibo;

        private Task(int num) {
            this.num = num;
        }

        @Override
        public void run() {
            if (num == 0) {
                fibo = 0;
                return;
            }
            long[] memo = new long[num + 1];
            memo[0] = 0;
            memo[1] = 1;
            for (int i = 2; i < memo.length; i++) {
                memo[i] = memo[i - 1] + memo[i - 2];
            }
            fibo = memo[memo.length - 1];
        }

        private long getFibo() {
            return fibo;
        }

        private int getNum() {
            return num;
        }

    }

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

    @Test
    @DisplayName("Testing thread pool in action.")
    void testFibo() throws Exception {
        //Given
        Map<Integer, Long> inputAndExpectedMapping = new HashMap<>() {
            {
                put(0, 0L);
                put(1, 1L);
                put(2, 1L);
                put(3, 2L);
                put(4, 3L);
                put(5, 5L);
                put(6, 8L);
                put(7, 13L);
                put(10, 55L);
                put(13, 233L);
            }
        };
        //When
        List<Task> runnableList = new ArrayList<>();
        runnableList.add(new Task(0));
        runnableList.add(new Task(1));
        runnableList.add(new Task(2));
        runnableList.add(new Task(3));
        runnableList.add(new Task(4));
        runnableList.add(new Task(5));
        runnableList.add(new Task(6));
        runnableList.add(new Task(7));
        runnableList.add(new Task(10));
        runnableList.add(new Task(13));
        try (ThreadPool pool = FixedThreadPool.create(Runtime.getRuntime().availableProcessors())) {
            pool.start();
            runnableList.forEach(pool::execute);
        }
        runnableList.forEach(task -> assertThat(task.getFibo()).isEqualTo(inputAndExpectedMapping.get(task.getNum())));
    }

    @Test
    @DisplayName("Testing thread pool auto - closing.")
    void testAutoClosing() throws InterruptedException {
        List<Runnable> allTasks = getRunnables();
        Thread poolThread = new Thread(() -> {
            try (ThreadPool pool = FixedThreadPool.create(Runtime.getRuntime().availableProcessors())) {
                allTasks.forEach(pool::execute);
            } catch (Exception e) {
                throw new RuntimeException("Error occurs.", e);
            }
        });
        poolThread.start();
        poolThread.join();
        assertThat(poolThread.isAlive()).isFalse();
    }

    @NotNull
    private static List<Runnable> getRunnables() {
        Runnable countingTask = () -> {
            int result = 0;
            for (int i = 0; i < 100; i++) {
                result += i;
            }
            LOGGER.info(String.format("Result: %d", result));
        };
        Runnable loggingTask = () -> LOGGER.info("This is a test log.");
        List<Runnable> allTasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            allTasks.add(countingTask);
        }
        for (int i = 0; i < 2; i++) {
            allTasks.add(loggingTask);
        }
        return allTasks;
    }

}
