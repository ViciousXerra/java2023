package edu.hw5.task3;

import edu.hw5.task3.parsers.DateParser;
import edu.hw5.task3.parsers.FullDateParser;
import edu.hw5.task3.parsers.RelativeCountDaysDateParser;
import edu.hw5.task3.parsers.RelativeDateParser;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Pattern;

public final class Task3 {

    private Task3() {

    }

    public static Optional<LocalDate> parseDate(String dateToParse) {
        DateParser parserChain = getParsersChain();
        return parserChain.parseDate(dateToParse);
    }

    private static DateParser getParsersChain() {
        DateParser parser = new FullDateParser(
            null,
            Pattern.compile("^(19[7-9]\\d|20\\d{2})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$"),
            DateTimeFormatter.ISO_LOCAL_DATE
        );
        parser = new FullDateParser(
            parser,
            Pattern.compile("^(19[7-9]\\d|20\\d{2})-(0[1-9]|1[0-2])-[1-31]$"),
            DateTimeFormatter.ofPattern("yyyy-MM-d")
        );
        parser = new FullDateParser(
            parser,
            Pattern.compile("^([1-9]|[12]\\d|3[01])/([1-9]|1[0-2])/(19[7-9]\\d|20\\d{2})$"),
            DateTimeFormatter.ofPattern("d/M/yyyy")
        );
        parser = new FullDateParser(
            parser,
            Pattern.compile("^([1-9]|[12]\\d|3[01])/([1-9]|1[0-2])/\\d{2}$"),
            DateTimeFormatter.ofPattern("d/M/uu")
        );
        parser = new RelativeDateParser(
            parser,
            Pattern.compile("^tomorrow$"),
            () -> {
                LocalDate localDate = LocalDate.now();
                return localDate.plusDays(1);
            }
        );
        parser = new RelativeDateParser(
            parser,
            Pattern.compile("^today$"),
            LocalDate::now
        );
        parser = new RelativeDateParser(
            parser,
            Pattern.compile("^yesterday$"),
            () -> {
                LocalDate localDate = LocalDate.now();
                return localDate.minusDays(1);
            }
        );
        parser = new RelativeDateParser(
            parser,
            Pattern.compile("^1 day ago$"),
            () -> {
                LocalDate localDate = LocalDate.now();
                return localDate.minusDays(1);
            }
        );
        parser = new RelativeCountDaysDateParser(
            parser,
            Pattern.compile("^(\\d+) days (ago|after)$")
        );
        return parser;
    }

}
