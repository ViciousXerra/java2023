package edu.hw3.task4;

import java.util.TreeMap;

public final class Task4 {

    private final static TreeMap<Integer, String> ROMAN_NUMERALS = new TreeMap<>() {
        {
            put(RomanNums.M.getValue(), RomanNums.M.name());
            put(RomanNums.CM.getValue(), RomanNums.CM.name());
            put(RomanNums.D.getValue(), RomanNums.D.name());
            put(RomanNums.CD.getValue(), RomanNums.CD.name());
            put(RomanNums.C.getValue(), RomanNums.C.name());
            put(RomanNums.XC.getValue(), RomanNums.XC.name());
            put(RomanNums.L.getValue(), RomanNums.L.name());
            put(RomanNums.XL.getValue(), RomanNums.XL.name());
            put(RomanNums.X.getValue(), RomanNums.X.name());
            put(RomanNums.IX.getValue(), RomanNums.IX.name());
            put(RomanNums.V.getValue(), RomanNums.V.name());
            put(RomanNums.IV.getValue(), RomanNums.IV.name());
            put(RomanNums.I.getValue(), RomanNums.I.name());
        }
    };

    private static final int DIGITS_COUNT_LIMIT = 4000;

    private Task4() {

    }

    public static String convertToRomanNumerals(int number) {
        boolean isValid = number > 0 && number < DIGITS_COUNT_LIMIT;
        if (!isValid) {
            throw new IllegalArgumentException("The number must be greater than zero and less than 4000.");
        }
        return recursiveBuilding(number);
    }

    private static String recursiveBuilding(int number) {
        int closestNum = ROMAN_NUMERALS.floorKey(number);
        if (number == closestNum) {
            return ROMAN_NUMERALS.get(closestNum);
        } else {
            return ROMAN_NUMERALS.get(closestNum) + recursiveBuilding(number - closestNum);
        }
    }

}
