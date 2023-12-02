package edu.hw8.task3;

import java.util.ArrayList;
import java.util.List;

public class PasswordGenerator {

    private final static int DIGIT_LOWER_BOUND_UNICODE = 0x0030;
    private final static int DIGIT_UPPER_BOUND_UNICODE = 0x0039;
    private final static int UPPERCASE_A_BOUND_UNICODE = 0x0041;
    private final static int UPPERCASE_Z_BOUND_UNICODE = 0x005A;
    private final static int LOWERCASE_A_BOUND_UNICODE = 0x0061;
    private final static int LOWERCASE_Z_BOUND_UNICODE = 0x007A;

    private final int length;
    private final StringBuilder builder;
    private final static int PASSWORDS_QUANTITY = 1000;

    public PasswordGenerator(int passwordLength) {
        if (passwordLength <= 0) {
            throw new IllegalArgumentException("Password length must be positive num.");
        }
        this.length = passwordLength;
        builder = new StringBuilder(length);
        builder.append((char) DIGIT_LOWER_BOUND_UNICODE);
    }

    public boolean hasNextPassword() {
        return builder.length() <= length;
    }

    public String nextPassword() {
        String res = builder.toString();
        setChars();
        return res;
    }

    public synchronized List<String> grabFewPasswords() {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < PASSWORDS_QUANTITY; i++) {
            if (hasNextPassword()) {
                res.add(this.nextPassword());
            } else {
                break;
            }
        }
        return res;
    }

    private void setChars() {
        int index = builder.length() - 1;
        char c = builder.charAt(index);
        if (c == (char) DIGIT_UPPER_BOUND_UNICODE) {
            builder.setCharAt(index, (char) UPPERCASE_A_BOUND_UNICODE);
        } else if (c == (char) UPPERCASE_Z_BOUND_UNICODE) {
            builder.setCharAt(index, (char) LOWERCASE_A_BOUND_UNICODE);
        } else if (c == (char) LOWERCASE_Z_BOUND_UNICODE) {
            resetWithPreviousChars();
        } else {
            builder.setCharAt(index, (char) (c + 1));
        }
    }

    private void resetWithPreviousChars() {
        int index = builder.length() - 1;
        while (index >= 0) {
            if (builder.charAt(index) == (char) LOWERCASE_Z_BOUND_UNICODE) {
                builder.setCharAt(index, (char) DIGIT_LOWER_BOUND_UNICODE);
                if (index == 0) {
                    builder.insert(index, (char) DIGIT_LOWER_BOUND_UNICODE);
                }
            } else {
                if (builder.charAt(index) == (char) DIGIT_UPPER_BOUND_UNICODE) {
                    builder.setCharAt(index, (char) UPPERCASE_A_BOUND_UNICODE);
                } else if (builder.charAt(index) == (char) UPPERCASE_Z_BOUND_UNICODE) {
                    builder.setCharAt(index, (char) LOWERCASE_A_BOUND_UNICODE);
                } else {
                    builder.setCharAt(index, (char) (builder.charAt(index) + 1));
                }
                break;
            }
            index--;
        }
    }

}
