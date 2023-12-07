package edu.hw10.task1.classes;

import edu.hw10.task1.annotations.MaxValue;

public record SomeRecord(String value, @MaxValue(65535) int value1, int value2) implements Randomizable {
}
