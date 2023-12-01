package edu.hw9.task1.StatGenerators;

public class SumDoubleStatGenerator implements DoubleDataStatGenerator {

    private double sum;

    @Override
    public void apply(double... values) {
        validate(values);
        for (double d : values) {
            sum += d;
        }
    }

    @Override
    public Double getStat() {
        return sum;
    }
}
