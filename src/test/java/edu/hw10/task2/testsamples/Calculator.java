package edu.hw10.task2.testsamples;

import edu.hw10.task2.cacheproxy.Cache;

public interface Calculator {

    @Cache(persist = true, filePath = "src/test/resources/hw10testresources/fibonaccicache.txt")
    long calc(int num);

}
