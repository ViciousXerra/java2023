package edu.hw8.task2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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
                        Runnable r = runnables.take();
                        r.run();
                    } catch (InterruptedException e) {
                        break;
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
        try {
            runnables.put(r);
        } catch (InterruptedException ignored) {
//            throw new RuntimeException("Thread has been interrupted while waiting Runnable insertion.", e);
        }
//        Thread insertionThread = new Thread(() -> {
//
//        });
//        insertionThread.start();
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
