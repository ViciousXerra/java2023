package edu.hw3.task2;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

public final class Task2 {

    private final static char OPENING_BRACKET = '(';
    private final static char CLOSING_BRACKET = ')';

    private Task2() {

    }

    /**
     * Returns array of String values with balanced count of opening and closing brackets.
     *
     * @param input processing String value
     * @return array of substrings. if input is null, or else contains other characters, or contains opening
     *     brackets without pair - returns empty array
     */
    public static String[] clusterize(String input) {
        boolean isValid = input != null && input.length() > 1 && Pattern.matches("[()]++", input);
        if (!isValid) {
            return new String[0];
        }
        Stack<Character> stack = new Stack<>();
        StringBuilder builder = new StringBuilder();
        List<String> clusters = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            builder.append(input.charAt(i));
            switch (input.charAt(i)) {
                case OPENING_BRACKET -> stack.push(input.charAt(i));
                case CLOSING_BRACKET -> {
                    if (stack.empty()) {
                        return new String[0];
                    } else {
                        stack.pop();
                        if (stack.empty()) {
                            clusters.add(builder.toString());
                            builder.setLength(0);
                        }
                    }
                }
                default -> {
                    return new String[0];
                }
            }
        }
        return clusters.toArray(String[]::new);
    }

}
