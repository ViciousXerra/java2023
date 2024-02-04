package edu.hw10.task1.testSamples;

import edu.hw10.task1.annotations.MaxValue;

public class TestClass2 {

    private final int value;

    public TestClass2(@MaxValue(0) int value) {
        this.value = value;
    }

}

