package edu.hw5.task3.parsers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FullDateParser extends AbstractDateParser {

    private final DateTimeFormatter formatter;

    public FullDateParser(DateParser nextParser, Pattern pattern, DateTimeFormatter formatter) {
        super(nextParser, pattern);
        this.formatter = formatter;
    }

    @Override
    final LocalDate getLocalDate(String dateToParse, Matcher matcher) {
        return LocalDate.parse(dateToParse, formatter);
    }

}
