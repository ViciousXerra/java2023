package edu.hw8.task2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class FixedThreadPool implements ThreadPool {

    private final static long TIMEOUT = 1L;
    private final BlockingQueue<Runnable> runnables;
    private final Thread[] pool;
    private volatile boolean isShuttedDown;

    public static FixedThreadPool create(int nThreads) {
        if (nThreads <= 0) {
            throw new IllegalArgumentException("Number of threads must be a positive num.");
        }
        return new FixedThreadPool(nThreads);
    }

    private FixedThreadPool(int nThreads) {
        pool = new Thread[nThreads];
        runnables = new ArrayBlockingQueue<>(nThreads, true);
        for (int i = 0; i < pool.length; i++) {
            pool[i] = new Thread(() -> {
                while (!isShuttedDown) {
                    //blocking until new runnable would be inserted by execute method.
                    try {
                        Runnable r = runnables.poll(TIMEOUT, TimeUnit.SECONDS);
                        if (r == null) {
                            continue;
                        }
                        r.run();
                    } catch (InterruptedException ignored) {

                    }
                }
            });
        }

    }

    @Override
    public void start() {
        for (Thread thread : pool) {
            thread.start();
        }
    }

    @Override
    public void execute(Runnable r) {
        if (r == null) {
            throw new IllegalArgumentException("Runnable can't be null.");
        }
        Thread insertionThread = new Thread(() -> {
            try {
                runnables.put(r);
            } catch (InterruptedException e) {
                throw new RuntimeException("Thread has been interrupted while waiting Runnable insertion.", e);
            }
        });
        insertionThread.start();
    }

    @Override
    public void close() {
        isShuttedDown = true;
        awaitAllThreadsTermination();
    }

    private void awaitAllThreadsTermination() {
        for (Thread t : pool) {
            try {
                t.interrupt();
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(
                    "Thread has been interrupted while waiting all in-pool threads termination.",
                    e
                );
            }

        }
    }

}
