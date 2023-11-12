package edu.hw3.task1;

import java.util.HashMap;
import java.util.Map;

public final class Task1 {

    private final static int UPPERCASE_A_UNICODE = 0x0041;
    private final static int UPPERCASE_Z_UNICODE = 0x005A;
    private final static int LOWERCASE_A_UNICODE = 0x0061;
    private final static int LOWERCASE_Z_UNICODE = 0x007A;

    private final static Map<Character, Character> KEY_MAPPING = new HashMap<>() {
        {
            for (int i = 0; i <= UPPERCASE_Z_UNICODE - UPPERCASE_A_UNICODE; i++) {
                put((char) (UPPERCASE_A_UNICODE + i), (char) (UPPERCASE_Z_UNICODE - i));
            }
            for (int i = 0; i <= LOWERCASE_Z_UNICODE - LOWERCASE_A_UNICODE; i++) {
                put((char) (LOWERCASE_A_UNICODE + i), (char) (LOWERCASE_Z_UNICODE - i));
            }
        }
    };

    private Task1() {

    }

    public static String atbashCipher(String toEncrypt) {
        if (toEncrypt == null || toEncrypt.isEmpty()) {
            return "";
        }
        char[] letters = new char[toEncrypt.length()];
        for (int i = 0; i < toEncrypt.length(); i++) {
            letters[i] = KEY_MAPPING.getOrDefault(toEncrypt.charAt(i), toEncrypt.charAt(i));
        }
        return new String(letters);
    }

}
