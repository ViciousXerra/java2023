package edu.hw5.task3.parsers;

import java.time.LocalDate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RelativeDateParser extends AbstractDateParser {

    private final Supplier<LocalDate> localDateSupplier;

    public RelativeDateParser(DateParser nextParser, Pattern pattern, Supplier<LocalDate> localDateSupplier) {
        super(nextParser, pattern);
        this.localDateSupplier = localDateSupplier;
    }

    @Override
    LocalDate getLocalDate(String dateToParse, Matcher matcher) {
        return localDateSupplier.get();
    }

}
