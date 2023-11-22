package edu.hw7;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public final class Task1 {

    private Task1() {

    }

    public static long countWithMultipleThreads(int numOfThreads, long initialValue) {
        if (numOfThreads < 1) {
            throw new IllegalArgumentException("Number of threads can't be less than 1.");
        }
        MultiThreadCounter counter = new MultiThreadCounter(initialValue);
        ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
        Runnable task = counter::increment;
        for (int j = 0; j < numOfThreads; j++) {
            executor.execute(task);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        return counter.getResult();
    }

    public static class MultiThreadCounter {

        private final AtomicLong counter;

        public MultiThreadCounter(long initialValue) {
            this.counter = new AtomicLong(initialValue);
        }

        public void increment() {
            counter.incrementAndGet();
        }

        public long getResult() {
            return counter.get();
        }

    }

}
