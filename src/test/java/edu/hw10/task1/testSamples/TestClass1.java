package edu.hw10.task1.testSamples;

import edu.hw10.task1.annotations.MinValue;

public class TestClass1 {

    private final int value;

    public TestClass1(@MinValue(0) int value) {
        this.value = value;
    }

}
