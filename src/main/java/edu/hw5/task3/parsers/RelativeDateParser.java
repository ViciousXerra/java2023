package edu.hw5.task3.parsers;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;

class RelativeDateParser extends AbstractDateParser {

    private final Supplier<LocalDate> localDateSupplier;


    RelativeDateParser(DateParser nextParser, Pattern pattern, Supplier<LocalDate> localDateSupplier) {
        super(nextParser, pattern);
        this.localDateSupplier = localDateSupplier;
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
        RelativeDateParser that = (RelativeDateParser) o;
        return localDateSupplier.equals(that.localDateSupplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), localDateSupplier);
    }

    @Override
    LocalDate getLocalDate(String dateToParse) {
        return localDateSupplier.get();
    }

}
