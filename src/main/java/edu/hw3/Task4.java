package edu.hw3;

import java.util.TreeMap;

public final class Task4 {

    private final static TreeMap<Integer, String> ROMAN_LITERALS = new TreeMap<>() {
        {
            put(1000, "M");
            put(900, "CM");
            put(500, "D");
            put(400, "CD");
            put(100, "C");
            put(90, "XC");
            put(50, "L");
            put(40, "XL");
            put(10, "X");
            put(9, "IX");
            put(5, "V");
            put(4, "IV");
            put(1, "I");
        }
    };

    private Task4() {

    }

    public static String convertToRomanNumerals(int number) {
        boolean isValid = number > 0 && number < 4000;
        if (!isValid) {
            throw new IllegalArgumentException("The number must be greater than zero and less than 4000.");
        }
        return recursiveBuilding(number);
    }

    private static String recursiveBuilding(int number) {
        int closestNum = ROMAN_LITERALS.floorKey(number);
        if (number == closestNum) {
            return ROMAN_LITERALS.get(closestNum);
        } else {
            return ROMAN_LITERALS.get(closestNum) + recursiveBuilding(number - closestNum);
        }
    }

}
