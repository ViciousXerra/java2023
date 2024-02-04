package edu.hw10.task1.testSamples;

import edu.hw10.task1.annotations.MaxValue;
import edu.hw10.task1.annotations.MinValue;

public record TestRecord(int value1, @MinValue(0) int value2, @MaxValue(0) int value3) {
}
