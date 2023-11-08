package edu.hw5.task3.parsers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Pattern;

class FullDateParser extends AbstractDateParser {

    private final DateTimeFormatter formatter;

    FullDateParser(DateParser nextParser, Pattern pattern, DateTimeFormatter formatter) {
        super(nextParser, pattern);
        this.formatter = formatter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        FullDateParser that = (FullDateParser) o;
        return formatter.equals(that.formatter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), formatter);
    }

    @Override
    final LocalDate getLocalDate(String dateToParse) {
        return LocalDate.parse(dateToParse, formatter);
    }

}
