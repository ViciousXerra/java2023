package edu.hw9.task1.StatGenerators;

public class AverageDoubleStatGenerator implements DoubleDataStatGenerator {

    private long size;
    private double average;

    @Override
    public void apply(double... values) {
        validate(values);
        double sum = 0;
        for (double d : values) {
            sum += d;
        }
        if (size == 0 && values.length == 0) {
            return;
        }
        average = (average * size + sum) / (size + values.length);
        size += values.length;
    }

    @Override
    public Double getStat() {
        return average;
    }
}
