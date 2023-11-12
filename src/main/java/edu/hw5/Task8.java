package edu.hw5;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Task8 {

    private final static String NULL_STRING_MESSAGE = "String arg can't be null.";
    private final static String EMPTY_BLANK_MESSAGE = "String arg can't have length of 0 or contains only whitespaces";

    private Task8() {

    }

    public static boolean isOddLength(String input) {
        validation(input);
        return getResult(Pattern.compile("^([01]{2})*[01]$"), input);
    }

    public static boolean isStartsWithZeroAndOddLengthOrOneAndEvenLength(String input) {
        validation(input);
        return getResult(Pattern.compile("^(0([01]{2})*|1([01]{2})*[01])$"), input);
    }

    public static boolean isZeroCountMultiplesThree(String input) {
        validation(input);
        return getResult(Pattern.compile("^((1*01*){3})+$"), input);
    }

    public static boolean isNot11or111(String input) {
        validation(input);
        return getResult(Pattern.compile("^(?=[01]+$)(?!(11$|111$))"), input);
    }

    public static boolean isOddCharIsOne(String input) {
        validation(input);
        return getResult(Pattern.compile("^1([01]1?)*$"), input);
    }

    public static boolean isContainsNotLessThanTwoZerosAndNoMoreOneOne(String input) {
        validation(input);
        return getResult(Pattern.compile("(^0{2,}1?$)|(^1?0{2,}$)|(^0+1?0+$)"), input);
    }

    public static boolean isNotContainsSequencedOnes(String input) {
        validation(input);
        return getResult(Pattern.compile("^1?(0+1?)*$"), input);
    }

    private static void validation(String input) {
        if (input == null) {
            throw new IllegalArgumentException(NULL_STRING_MESSAGE);
        }
        if (input.isEmpty() || input.isBlank()) {
            throw new IllegalArgumentException(EMPTY_BLANK_MESSAGE);
        }
    }

    private static boolean getResult(Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

}
