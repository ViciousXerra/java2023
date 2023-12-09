package edu.hw10.task2.testsamples;

import edu.hw10.task2.cacheproxy.Cache;

public interface NumberDivider {

    @Cache(persist = true, filePath = "src/test/resources/hw10testresources/dividercache.txt")
    Double divide(Number num, Number divideBy);

}
