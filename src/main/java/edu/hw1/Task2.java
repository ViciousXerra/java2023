package edu.hw1;

public final class Task2 {

    private final static int DECIMAL_BASE = 10;

    private Task2() {

    }

    public static int countDigits(int num) {
        int count = 1;
        int value = num;
        while ((value /= DECIMAL_BASE) != 0) {
            count++;
        }
        return count;
    }
}
