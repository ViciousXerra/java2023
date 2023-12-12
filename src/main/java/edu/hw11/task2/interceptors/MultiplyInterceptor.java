package edu.hw11.task2.interceptors;

public final class MultiplyInterceptor {

    private MultiplyInterceptor() {

    }

    public static int intercept(int value1, int value2) {
        return value1 * value2;
    }

}
