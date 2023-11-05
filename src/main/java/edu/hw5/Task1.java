package edu.hw5;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Task1 {

    private Task1() {

    }

    /*public static Duration getAverageSessionTime(String... sessionTimes) {
        Pattern sessionTimePattern = getSessionTimePattern();
        Matcher matcher;
        int totalMinutes = 0;
        int totalTimes = 0;
        for (String time : sessionTimes) {
            if (time == null) {
                throw new IllegalArgumentException();
            }
            matcher = sessionTimePattern.matcher(time);
            if (matcher.hasMatch()) {

            }
        }
    }*/

    private static Pattern getSessionTimePattern() {
        String dateRegex = "20\\d{2}-(0[1-9])|(1[0-2])-(0[1-9])|([12]\\d)|(3[01])";
        String timeRegex = "([01]\\d)|(2[0-3]):[0-5]\\d";
        String fullRegex = dateRegex + ", " + timeRegex;
        return Pattern.compile(fullRegex + " - " + fullRegex);
    }

}
