package edu.hw10.task2.classes;

public class BasicNumberDivider implements NumberDivider {

    @Override
    public Double divide(Number num, Number divideBy) {
        if (divideBy.doubleValue() == 0) {
            throw new ArithmeticException("Divide by zero attempt.");
        }
        return num.doubleValue() / divideBy.doubleValue();
    }

}
