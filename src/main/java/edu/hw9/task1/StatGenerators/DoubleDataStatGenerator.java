package edu.hw9.task1.StatGenerators;

public interface DoubleDataStatGenerator extends NumericalDataStatGenerator<Double> {

    void apply(double... values);

    default void validate(double... values) {
        if (values == null) {
            throw new IllegalArgumentException("Values var-args can't be null");
        }
        if (values.length == 0) {
            throw new IllegalStateException("Values length must be at least 1 element.");
        }
    }

}
