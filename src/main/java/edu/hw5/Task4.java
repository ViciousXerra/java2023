package edu.hw5;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Task4 {

    private final static String NULL_PASSWORD_MESSAGE = "Password can't be null.";

    private Task4() {

    }

    public static boolean isPasswordValid(String password) {
        if (password == null) {
            throw new IllegalArgumentException(NULL_PASSWORD_MESSAGE);
        }
        Pattern validationPattern = Pattern.compile("^(?=.*?[~!@#$%^&*|]).+$");
        Matcher matcher = validationPattern.matcher(password);
        return matcher.find();
    }

}
