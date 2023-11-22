package edu.hw7;

import java.util.stream.LongStream;

public final class Task2 {

    private final static int FACTORIAL_LIMIT = 20;

    private Task2() {

    }

    public static long getFactorialWithParallelStream(int num) {
        if (num > FACTORIAL_LIMIT) {
            throw new IllegalArgumentException();
        }
        return LongStream.range(1, num + 1).parallel().reduce((a, b) -> a * b).orElseThrow();
    }

}
