package edu.hw5.task3.parsers;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
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
        if (pattern.matcher(dateToParse).find()) {
            return Optional.ofNullable(getLocalDate(dateToParse));
        } else if (nextParser != null) {
            return nextParser.parseDate(dateToParse);
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractDateParser that = (AbstractDateParser) o;
        return pattern.equals(that.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern);
    }

    abstract LocalDate getLocalDate(String dateToParse);

}
