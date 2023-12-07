package edu.hw10.task2.classes;

import edu.hw10.task2.cacheproxy.Cache;

public interface NumberDivider {

    @Cache(persist = true, filePath = "src/main/resources/hw10resources/dividercache.txt")
    Double divide(Number num, Number divideBy);

}
