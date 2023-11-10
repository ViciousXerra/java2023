package edu.hw5.task3.parsers;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class AbstractDateParser implements DateParser {

    private final static String NULL_RESTRICTION = "Date string can't be null.";

    protected Pattern pattern;
    private final DateParser nextParser;


    AbstractDateParser(DateParser nextParser, Pattern pattern) {
        this.nextParser = nextParser;
        this.pattern = pattern;
    }

    @Override
    public final Optional<LocalDate> parseDate(String dateToParse) {
        if (dateToParse == null) {
            throw new IllegalArgumentException(NULL_RESTRICTION);
        }
        Matcher matcher = pattern.matcher(dateToParse);
        if (matcher.find()) {
            return Optional.ofNullable(getLocalDate(dateToParse, matcher));
        } else if (nextParser != null) {
            return nextParser.parseDate(dateToParse);
        }
        return Optional.empty();
    }

    abstract LocalDate getLocalDate(String dateToParse, Matcher matcher);

}
