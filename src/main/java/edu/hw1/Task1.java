package edu.hw1;

import java.util.Arrays;

public final class Task1 {

    private final static int SECS_PER_MINUTE = 60;

    private Task1() {

    }

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
        return converted[1] >= SECS_PER_MINUTE ? -1 : converted[0] * SECS_PER_MINUTE + converted[1];
    }
}
