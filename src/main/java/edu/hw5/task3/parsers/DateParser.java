package edu.hw5.task3.parsers;

import java.time.LocalDate;
import java.util.Optional;

public interface DateParser {
    Optional<LocalDate> parseDate(String dateToParse);
}
