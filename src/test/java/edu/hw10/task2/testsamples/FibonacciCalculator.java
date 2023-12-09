package edu.hw10.task2.testsamples;

public class FibonacciCalculator implements Calculator {

    @Override
    public String toString() {
        return "This is fibonacci calculator.";
    }

    @Override
    public long calc(int num) {
        if (num < 0) {
            throw new IllegalArgumentException("Num argument must be not negative value.");
        }
        if (num == 0) {
            return 0;
        }
        long[] memo = new long[num + 1];
        memo[0] = 0;
        memo[1] = 1;
        for (int i = 2; i < memo.length; i++) {
            memo[i] = memo[i - 1] + memo[i - 2];
        }
        return memo[memo.length - 1];
    }

}
