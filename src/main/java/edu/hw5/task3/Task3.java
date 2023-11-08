package edu.hw5.task3;

import edu.hw5.task3.parsers.DateParser;
import edu.hw5.task3.parsers.DateParsersChainBuilder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Pattern;

public final class Task3 {

    private Task3() {

    }

    public Optional<LocalDate> parseDate(String dateToParse) {
        DateParser parserChain = getParsersChain();
        return parserChain.parseDate(dateToParse);
    }

    private static DateParser getParsersChain() {
        DateParsersChainBuilder builder = new DateParsersChainBuilder();
        builder
            .apply(
                Pattern.compile("^(19[7-9]\\d|20\\d{2})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$"),
                DateTimeFormatter.ISO_LOCAL_DATE
            )
            .apply(
                Pattern.compile("^(19[7-9]\\d|20\\d{2})-(0[1-9]|1[0-2])-[1-31]$"),
                DateTimeFormatter.ofPattern("yyyy-MM-d")
            )
            .apply(
                Pattern.compile("^([1-9]|[12]\\d|3[01])/([1-9]|1[0-2])/(19[7-9]\\d|20\\d{2})$"),
                DateTimeFormatter.ofPattern("d/M/yyyy")
            )
            .apply(
                Pattern.compile("^([1-9]|[12]\\d|3[01])/([1-9]|1[0-2])/\\d{2}$"),
                DateTimeFormatter.ofPattern("d/M/uu")
            )
            .apply(
                Pattern.compile("^tomorrow$"),
                () -> {
                    LocalDate localDate = LocalDate.now();
                    return localDate.plusDays(1);
                }
            )
            .apply(
                Pattern.compile("^today$"),
                LocalDate::now
            )
            .apply(
                Pattern.compile("^yesterday$"),
                () -> {
                    LocalDate localDate = LocalDate.now();
                    return localDate.minusDays(1);
                }
            )
            .apply(
                Pattern.compile("^1 day ago$"),
                () -> {
                    LocalDate localDate = LocalDate.now();
                    return localDate.minusDays(1);
                }
            )
            .apply(
                Pattern.compile("^(\\d+) days (ago|after)$")
            );
        return builder.getLastInstance();
    }

}
