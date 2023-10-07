package edu.hw1;

public final class Task4 {

    private Task4() {

    }

    public static String fixString(String str) {
        if (str == null) {
            return "DEFAULT";
        }
        StringBuilder sb = new StringBuilder();
        int i;
        for (i = 0; i < str.length() - 1; i += 2) {
            sb.append(str.charAt(i + 1));
            sb.append(str.charAt(i));
        }
        if (i == str.length() - 1) {
            sb.append(str.charAt(i));
        }
        return sb.toString();
    }
}
