package edu.hw10.task1.classes;

import edu.hw10.task1.annotations.MinValue;
import edu.hw10.task1.annotations.NotNull;

public class SomeClass implements Randomizable {

    private final String stringvValue;
    private final int intValue;

    public SomeClass(@NotNull String stringValue, @MinValue(100) int intValue) {
        this.stringvValue = stringValue;
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringvValue;
    }

    public int getIntValue() {
        return intValue;
    }

}
