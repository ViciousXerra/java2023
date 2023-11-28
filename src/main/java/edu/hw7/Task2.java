package edu.hw7;

import java.util.NoSuchElementException;
import java.util.stream.LongStream;

public final class Task2 {

    private final static int FACTORIAL_LIMIT = 20;
    private final static String FACTORIAL_LIMIT_RESTRICTION_MESSAGE =
        "Method does not support numbers larger than 20.";
    private final static String NEGATIVE_NUMBER_MESSAGE =
        "Method does not support negative integers and gamma function for complex numbers.";

    private Task2() {

    }

    public static long getFactorialWithParallelStream(int num) {
        if (num > FACTORIAL_LIMIT) {
            throw new IllegalArgumentException(FACTORIAL_LIMIT_RESTRICTION_MESSAGE);
        } else if (num < 0) {
            throw new IllegalArgumentException(NEGATIVE_NUMBER_MESSAGE);
        }
        if (num == 0) {
            return 1;
        }
        return LongStream
            .range(1, num + 1)
            .parallel()
            .reduce((a, b) -> a * b)
            .orElseThrow(() -> new NoSuchElementException("Unable to resolve result."));
    }

}
