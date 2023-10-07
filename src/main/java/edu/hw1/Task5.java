package edu.hw1;

import static java.lang.Character.getNumericValue;

public final class Task5 {

    private final static int DECIMAL_BASE = 10;
    private final static int SUMMING_UP_STRING_LEN_lIMIT = 3;

    private Task5() {

    }

    private static boolean checkDescendant(int num) {
        int number = num;
        boolean result = false;
        while (number >= DECIMAL_BASE) {
            result = number == reverseNum(number);
            if (result || String.valueOf(number).length() == SUMMING_UP_STRING_LEN_lIMIT) {
                break;
            }
            number = sumUp(number);
        }
        return result;
    }

    private static int reverseNum(int num) {
        StringBuilder builder = new StringBuilder(String.valueOf(num));
        builder.reverse();
        return Integer.parseInt(builder.toString());
    }

    private static int sumUp(int num) {
        String value = String.valueOf(num);
        if (value.length() % 2 == 0) {
            return Integer.parseInt(sumUp(value));
        }
        StringBuilder builder = new StringBuilder();
        int mid = value.length() >> 1;
        builder
            .append(sumUp(value.substring(0, mid)))
            .append(value.charAt(mid))
            .append(sumUp(value.substring(mid + 1)));
        return Integer.parseInt(builder.toString());
    }

    private static String sumUp(String str) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i += 2) {
            builder.append(getNumericValue(str.charAt(i)) + getNumericValue(str.charAt(i + 1)));
        }
        return builder.toString();
    }

    public static boolean isPalindromeDescendant(int num) {
        int number = num;
        if (number < 0) {
            number *= -1;
        }
        return number < DECIMAL_BASE || checkDescendant(number);
        /*
        Если изначально было передано число по модулю < 10,
        оно будет считаться палиндромом
        */
    }
}
