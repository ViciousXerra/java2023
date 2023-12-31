package edu.hw5.task3.parsers;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RelativeCountDaysDateParser extends AbstractDateParser {

    private final static int DAYS_MATCHER_GROUP = 1;
    private final static int RELATIVE_MATCHER_GROUP = 2;
    private final static String BEFORE_DAYS = "ago";
    private final static String AFTER_DAYS = "after";

    public RelativeCountDaysDateParser(DateParser nextParser, Pattern pattern) {
        super(nextParser, pattern);
    }

    @Override
    LocalDate getLocalDate(String dateToParse, Matcher matcher) {
        LocalDate localDate = LocalDate.now();
        long days = Long.parseLong(matcher.group(DAYS_MATCHER_GROUP));
        if (AFTER_DAYS.equals(matcher.group(RELATIVE_MATCHER_GROUP))) {
            return localDate.plusDays(days);
        } else {
            return localDate.minusDays(days);
        }
    }
}
