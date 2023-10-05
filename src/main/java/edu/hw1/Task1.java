package edu.hw1;

import java.util.Arrays;

public final class Task1 {

    private Task1() {

    }

    @SuppressWarnings("MagicNumber")
    public static long minutesToSeconds(String str) {
        String[] values;
        long[] converted;
        try {
            values = str.split(":");
            converted = Arrays.stream(values).mapToLong(Long::valueOf).toArray();
            if (converted.length != 2) {
                return -1;
            }
        } catch (NullPointerException | NumberFormatException e) {
            return -1;
        }
        return converted[1] > 59 ? -1 : converted[0] * 60 + converted[1];
    }
}
