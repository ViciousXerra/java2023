package edu.hw3;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

public final class Task2 {

    private Task2() {

    }

    public static String[] clusterize(@NotNull String input) {
        boolean isValid = input.length() > 1 && Pattern.matches("[()]++", input);
        if (!isValid) {
            return new String[0];
        }
        Stack<Character> stack = new Stack<>();
        StringBuilder builder = new StringBuilder();
        List<String> clusters = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            builder.append(input.charAt(i));
            if (input.charAt(i) == '(') {
                stack.push(input.charAt(i));
                continue;
            }
            if (input.charAt(i) == ')') {
                stack.pop();
                if (stack.empty()) {
                    clusters.add(builder.toString());
                    builder.setLength(0);
                }
            }
        }
        return clusters.toArray(String[]::new);
    }

}
