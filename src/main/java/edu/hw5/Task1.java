package edu.hw5;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Task1 {

    private final static String NULL_RESTRICTION = "%s can't be null.";
    private final static String CALC_IMPOSSIBLE_MESSAGE = "Unable to calculate average time.";
    private final static int MINUTES_PER_HOUR = 60;

    private Task1() {

    }

    public static Duration getAvgSessionTime(String... sessionTimes) {
        if (sessionTimes == null) {
            throw new IllegalArgumentException(String.format(NULL_RESTRICTION, "Passing arg"));
        }
        OptionalDouble avgMinutes = Arrays
            .stream(sessionTimes)
            .map(SessionDuration::new)
            .map(SessionDuration::getTimeDuration)
            .mapToLong(Duration::toMinutes)
            .filter(value -> value != 0)
            .average();
        long minutes = (long) avgMinutes.orElseThrow(() -> new NoSuchElementException(CALC_IMPOSSIBLE_MESSAGE));
        return Duration.ofHours(minutes / MINUTES_PER_HOUR).plusMinutes(minutes % MINUTES_PER_HOUR);
    }

    private static class SessionDuration {

        private final static String MISMATCH_MESSAGE = "Invalid format.";
        private final static String INVALID_PLACEMENT_MESSAGE = "Invalid start and end time placement.";

        private final static String DATE_TIME_REGEX =
            "((20\\d{2}|19[89]\\d)-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]), ([01]\\d|2[0-3]):[0-5]\\d)";
        private final static Pattern SESSION_PATTERN =
            Pattern.compile("^" + DATE_TIME_REGEX + " - " + DATE_TIME_REGEX + "$");
        private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");
        private final static int START_DATE_TIME_GROUP = 1;
        private final static int END_DATE_TIME_GROUP = 6;

        private static Matcher matcher;

        private final LocalDateTime startSessionTime;
        private final LocalDateTime endSessionTime;

        private SessionDuration(String log) {
            validate(log);
            startSessionTime = LocalDateTime.parse(matcher.group(START_DATE_TIME_GROUP), DATE_TIME_FORMATTER);
            endSessionTime = LocalDateTime.parse(matcher.group(END_DATE_TIME_GROUP), DATE_TIME_FORMATTER);
        }

        private static void validate(String sessionTime) {
            if (sessionTime == null) {
                throw new IllegalArgumentException(String.format(NULL_RESTRICTION, "Session string"));
            }
            matcher = SESSION_PATTERN.matcher(sessionTime);
            if (!matcher.find()) {
                throw new IllegalArgumentException(MISMATCH_MESSAGE);
            }
        }

        private Duration getTimeDuration() {
            if (startSessionTime.isAfter(endSessionTime)) {
                throw new IllegalArgumentException(INVALID_PLACEMENT_MESSAGE);
            }

            return Duration.between(startSessionTime, endSessionTime);
        }

    }

}
