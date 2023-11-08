package edu.hw5.task3.parsers;

import edu.hw5.task3.instanceschainbuilder.UniqueInstanceChainBuilder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public final class DateParsersChainBuilder extends UniqueInstanceChainBuilder<DateParser> {

    private final static String PASSING_NULL_ARG_MESSAGE = "Arguments can't be null.";

    public DateParsersChainBuilder apply(Pattern pattern, DateTimeFormatter formatter) {
        validate(pattern, formatter);
        FullDateParser parser = new FullDateParser(lastInstance, pattern, formatter);
        return apply(parser);
    }

    public DateParsersChainBuilder apply(Pattern pattern, Supplier<LocalDate> localDateSupplier) {
        validate(pattern, localDateSupplier);
        RelativeDateParser parser = new RelativeDateParser(lastInstance, pattern, localDateSupplier);
        return apply(parser);
    }

    public DateParsersChainBuilder apply(Pattern pattern) {
        validate(pattern);
        RelativeCountDaysDateParser parser = new RelativeCountDaysDateParser(lastInstance, pattern);
        return apply(parser);
    }

    @Override
    protected DateParsersChainBuilder apply(DateParser parserInstance) {
        if (isUnique(parserInstance)) {
            this.lastInstance = parserInstance;
            instances.add(parserInstance);
        }
        return this;
    }

    private void validate(Object... args) {
        for (Object arg : args) {
            if (arg == null) {
                throw new IllegalArgumentException(PASSING_NULL_ARG_MESSAGE);
            }
        }
    }

}
