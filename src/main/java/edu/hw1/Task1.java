package edu.hw1;

import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

public final class Task1 {

    private final static int SECS_PER_MINUTE = 60;

    private Task1() {

    }

    /**
     * This is a static method to convert String representation of time to total number of seconds.
     * @param str value to be converted
     * @return <p>positive long value or 0, representing time in seconds
     *     <p>-1 - if passed value don't match time regular expression ^(\d+):([0-5]?[0-9])$</p>
     * @throws IllegalArgumentException if the passed value can't be converted to a numeric value
     */
    public static long minutesToSeconds(@NotNull String str) {
        String[] values = str.split(":");
        if (values.length != 2) {
            return -1;
        }
        long[] converted;
        try {
            converted = Arrays.stream(values).mapToLong(Long::valueOf).toArray();
        } catch (NumberFormatException e) {
            return -1;
        }
        return converted[1] >= SECS_PER_MINUTE ? -1 : converted[0] * SECS_PER_MINUTE + converted[1];
    }

}
