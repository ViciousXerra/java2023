package edu.hw10.task2;

import edu.hw10.task2.cacheproxy.CacheProxy;
import edu.hw10.task2.classes.BasicNumberDivider;
import edu.hw10.task2.classes.Calculator;
import edu.hw10.task2.classes.FibonacciCalculator;
import edu.hw10.task2.classes.NumberDivider;

public class Demo {

    public static void main(String[] args) {
        Calculator fibo = new FibonacciCalculator();
        Calculator calcProxy = CacheProxy.getCacheProxyInstance(fibo, Calculator.class);
        System.out.println(calcProxy.calc(0));
        System.out.println(calcProxy.calc(1));
        System.out.println(calcProxy.calc(2));
        System.out.println(calcProxy.calc(3));
        System.out.println(calcProxy.calc(4));
        System.out.println(calcProxy.calc(5));
        System.out.println(calcProxy.calc(5));
        System.out.println(calcProxy.calc(4));
        System.out.println(calcProxy.calc(3));
        System.out.println(calcProxy.calc(2));
        System.out.println(calcProxy.calc(1));
        System.out.println(calcProxy.calc(10));
//        NumberDivider divider = new BasicNumberDivider();
//        NumberDivider dividerProxy = CacheProxy.getCacheProxyInstance(divider, NumberDivider.class);
//        System.out.println(dividerProxy.divide(50, 2.5));
//        System.out.println(dividerProxy.divide(10, 2.5));
//        System.out.println(dividerProxy.divide(2, 3.1));
    }

}
