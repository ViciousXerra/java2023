package edu.hw10.task2.testsamples;

public class BasicNumberDivider implements NumberDivider {

    @Override
    public String toString() {
        return "This is basic number divider.";
    }

    @Override
    public Double divide(Number num, Number divideBy) {
        if (divideBy.doubleValue() == 0) {
            throw new ArithmeticException("Divide by zero attempt.");
        }
        return num.doubleValue() / divideBy.doubleValue();
    }

}
