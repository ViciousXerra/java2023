package edu.hw1;

public class Task2 {

    private Task2() {

    }

    @SuppressWarnings("MagicNumber")
    public static int countDigits(int num) {
        int count = 1;
        int value = num;
        while ((value /= 10) != 0) {
            count++;
        }
        return count;
    }
}
