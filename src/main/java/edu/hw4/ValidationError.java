package edu.hw4;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public final class ValidationError {

    private final static String TYPE_FIELD_DESCRIPTION = "\"type\" field is null.\n";
    private final static String SEX_FIELD_DESCRIPTION = "\"sex\" field is null.\n";
    private final static String AGE_FIELD_DESCRIPTION = "\"age\" field is less or equal to 0.\n";
    private final static String HEIGHT_FIELD_DESCRIPTION = "\"height\" field is less or equal to 0.\n";
    private final static String WEIGHT_FIELD_DESCRIPTION = "\"weight\" field is less or equal to 0.\n";

    private final String description;

    private ValidationError(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

    public static Set<ValidationError> getErrors(@NotNull Animal animal) {
        Set<ValidationError> errors = new HashSet<>();
        if (animal.type() == null) {
            errors.add(new ValidationError(TYPE_FIELD_DESCRIPTION));
        }
        if (animal.sex() == null) {
            errors.add(new ValidationError(SEX_FIELD_DESCRIPTION));
        }
        if (animal.age() <= 0) {
            errors.add(new ValidationError(AGE_FIELD_DESCRIPTION));
        }
        if (animal.height() <= 0) {
            errors.add(new ValidationError(HEIGHT_FIELD_DESCRIPTION));
        }
        if (animal.weight() <= 0) {
            errors.add(new ValidationError(WEIGHT_FIELD_DESCRIPTION));
        }
        return errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ValidationError error = (ValidationError) o;
        return description.equals(error.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
