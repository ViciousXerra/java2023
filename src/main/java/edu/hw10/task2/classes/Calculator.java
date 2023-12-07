package edu.hw10.task2.classes;

import edu.hw10.task2.cacheproxy.Cache;

public interface Calculator {

    @Cache(persist = true, filePath = "src/main/resources/hw10resources/fibonaccicache.txt")
    long calc(int num);

}
