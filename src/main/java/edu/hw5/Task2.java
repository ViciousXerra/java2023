package edu.hw5;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public final class Task2 {

    private final static String NULL_LOCAL_DATE_MESSAGE = "Local date can't be null.";
    private final static String INVALID_YEAR_MESSAGE = "Invalid year number";

    private final static int MONTH_COUNT_LIMIT = 13;
    private final static int LOOKING_DAY_COUNT = 13;

    private Task2() {

    }

    public static List<LocalDate> getAllFriday13(int year) {
        if (year < Year.MIN_VALUE || year > Year.MAX_VALUE) {
            throw new IllegalArgumentException(INVALID_YEAR_MESSAGE);
        }
        List<LocalDate> list = new ArrayList<>();
        for (int month = 1; month < MONTH_COUNT_LIMIT; month++) {
            LocalDate lookingDate = LocalDate.of(year, month, LOOKING_DAY_COUNT);
            if (lookingDate.getDayOfWeek() == DayOfWeek.FRIDAY) {
                list.add(lookingDate);
            }
        }
        return list;
    }

    public static LocalDate getClosestFriday13(LocalDate startingFrom) {
        if (startingFrom == null) {
            throw new IllegalArgumentException(NULL_LOCAL_DATE_MESSAGE);
        }
        return startingFrom.with(TemporalAdjusters.ofDateAdjuster(date -> {
            LocalDate nextFriday = date.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
            while (nextFriday.getDayOfMonth() != LOOKING_DAY_COUNT) {
                nextFriday = nextFriday.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
            }
            return nextFriday;
        }));
    }

}
