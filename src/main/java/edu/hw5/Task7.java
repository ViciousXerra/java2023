package edu.hw5;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Task7 {

    private final static String NULL_STRING_MESSAGE = "String arg can't be null.";

    private Task7() {

    }

    public static boolean lengthMoreOrEqualThreeAndThirdCharIsZero(String input) {
        validation(input);
        return getResult(Pattern.compile("^[01]{2}0[01]*$"), input);
    }

    public static boolean startsAndEndsWithSameChar(String input) {
        validation(input);
        return getResult(Pattern.compile("^(1[01]*1|0[01]*0)$"), input);
    }

    public static boolean lengthMoreOrEqualOneAndNoMoreThree(String input) {
        validation(input);
        return getResult(Pattern.compile("^[01]{1,3}$"), input);
    }

    private static void validation(String input) {
        if (input == null) {
            throw new IllegalArgumentException(NULL_STRING_MESSAGE);
        }
    }

    private static boolean getResult(Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

}
