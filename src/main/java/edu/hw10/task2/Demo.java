package edu.hw10.task2;

import edu.hw10.task2.cacheproxy.CacheProxy;
import edu.hw10.task2.classes.Calculator;
import edu.hw10.task2.classes.FibonacciCalculator;

public class Demo {

    public static void main(String[] args) {
        Calculator fibo = new FibonacciCalculator();
        Calculator calcProxy = CacheProxy.getCacheProxyInstance(fibo, Calculator.class);
        System.out.println(calcProxy.calc(0));
    }

}
