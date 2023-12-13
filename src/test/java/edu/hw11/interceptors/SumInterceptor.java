package edu.hw11.interceptors;

public final class SumInterceptor {

    private SumInterceptor() {

    }

    public static int intercept(int value1, int value2) {
        return value1 + value2;
    }

}
