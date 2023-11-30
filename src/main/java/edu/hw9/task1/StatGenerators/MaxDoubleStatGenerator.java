package edu.hw9.task1.StatGenerators;

public class MaxDoubleStatGenerator implements DoubleDataStatGenerator {

    private double max;

    @Override
    public void apply(double... values) {
        validate(values);
        if (max == 0) {
            double localMax = values[0];
            for (int i = 1; i < values.length; i++) {
                localMax = Math.max(localMax, values[i]);
                max = localMax;
            }
        } else {
            for (double d : values) {
                max = Math.min(max, d);
            }
        }
    }

    @Override
    public Double getStat() {
        return max;
    }
}
