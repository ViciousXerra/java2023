package edu.hw11.task2.interceptors;

public final class DivideInterceptor {

    private DivideInterceptor() {

    }

    public static int intercept(int value1, int value2) {
        return value1 / value2;
    }

}
