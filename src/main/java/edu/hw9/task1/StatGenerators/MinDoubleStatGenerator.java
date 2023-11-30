package edu.hw9.task1.StatGenerators;

public class MinDoubleStatGenerator implements DoubleDataStatGenerator {

    private double min;

    @Override
    public void apply(double... values) {
        validate(values);
        if (min == 0) {
            double localMin = values[0];
            for (int i = 1; i < values.length; i++) {
                localMin = Math.min(localMin, values[i]);
                min = localMin;
            }
        }
        else {
            for (double d : values) {
                min = Math.min(min, d);
            }
        }

    }

    @Override
    public Double getStat() {
        return min;
    }
}
