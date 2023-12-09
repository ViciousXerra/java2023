package edu.hw10.task1.utils;

import edu.hw10.task1.annotations.MaxValue;
import edu.hw10.task1.annotations.MinValue;
import java.lang.reflect.Parameter;
import java.util.Random;

public final class RandomValueUtils {

    private final static Random RANDOM = new Random();
    private final static int LOWER_ALPHABETIC_BOUND = 97;
    private final static int UPPER_ALPHABETIC_BOUND = 122;
    private final static int DEFAULT_STRING_LENGTH = 5;

    private RandomValueUtils() {

    }

    public static String generateRandomString() {
        return generateRandomString(DEFAULT_STRING_LENGTH);
    }

    public static String generateRandomString(int lengthTarget) {
        if (lengthTarget <= 0) {
            throw new IllegalArgumentException("String length must be a positive num.");
        }
        return RANDOM.ints(LOWER_ALPHABETIC_BOUND, UPPER_ALPHABETIC_BOUND + 1)
            .limit(lengthTarget)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    public static Object generateRandomInt(Parameter parameter) {
        if (parameter == null) {
            throw new IllegalArgumentException("Parameter instance can't be null.");
        }
        if (parameter.isAnnotationPresent(MinValue.class)) {
            return RandomValueUtils.resolveRandomInt(parameter.getAnnotation(MinValue.class).value(), false);
        }
        if (parameter.isAnnotationPresent(MaxValue.class)) {
            return RandomValueUtils.resolveRandomInt(parameter.getAnnotation(MaxValue.class).value(), true);
        }
        return RandomValueUtils.resolveRandomInt();
    }

    private static int resolveRandomInt() {
        return RANDOM.nextInt();
    }

    private static int resolveRandomInt(int pivot, boolean isLower) {
        return isLower ? RANDOM.nextInt(Integer.MIN_VALUE, pivot) : RANDOM.nextInt(pivot, Integer.MAX_VALUE);
    }

}
