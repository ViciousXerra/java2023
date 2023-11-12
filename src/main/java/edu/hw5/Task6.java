package edu.hw5;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Task6 {

    private final static String NULL_STRING_MESSAGE = "String arg can't be null.";
    private final static String EMPTY_BLANK_MESSAGE = "String arg can't have length of 0 or contains only whitespaces";
    private final static String WORD_BOUNDARY_ANCHOR = "\\b";

    private Task6() {

    }

    public static boolean isFirstArgIsSubsequenceOfSecond(String first, String second) {
        validation(first, second);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < first.length(); i++) {
            builder.append(".*");
            builder.append(first.charAt(i));
        }
        builder.append(".*");
        Pattern matchingPattern =
            Pattern.compile(WORD_BOUNDARY_ANCHOR + builder + WORD_BOUNDARY_ANCHOR, Pattern.DOTALL);
        Matcher matcher = matchingPattern.matcher(second);
        return matcher.find();
    }

    private static void validation(String first, String second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException(NULL_STRING_MESSAGE);
        }
        if (first.isEmpty() || first.isBlank()) {
            throw new IllegalArgumentException(EMPTY_BLANK_MESSAGE);
        }
        if (second.isEmpty() || second.isBlank()) {
            throw new IllegalArgumentException(EMPTY_BLANK_MESSAGE);
        }
    }

}
