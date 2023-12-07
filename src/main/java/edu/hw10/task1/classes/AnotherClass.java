package edu.hw10.task1.classes;

public class AnotherClass implements Randomizable {

    private final static int DEFAULT_VALUE = 15;

    private final int value;

    public AnotherClass() {
        value = DEFAULT_VALUE;
    }

    public int getValue() {
        return value;
    }

}
