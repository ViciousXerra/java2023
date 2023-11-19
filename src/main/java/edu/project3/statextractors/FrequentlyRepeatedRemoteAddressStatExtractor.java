package edu.project3.statextractors;

import edu.project3.logstreamextractors.LogRecord;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrequentlyRepeatedRemoteAddressStatExtractor implements StatExtractor<List<Map.Entry<String, Integer>>> {

    private final static int ADDRESS_MAX_SIZE = 3;
    private final Map<String, Integer> frequencyMap = new HashMap<>();

    @Override
    public List<Map.Entry<String, Integer>> getStat() {
        return frequencyMap
            .entrySet()
            .stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(ADDRESS_MAX_SIZE)
            .toList();
    }

    @Override
    public void accept(LogRecord logRecord) {
        int currentFreq = frequencyMap.getOrDefault(logRecord.remoteAddress(), 0);
        frequencyMap.put(logRecord.remoteAddress(), ++currentFreq);
    }
}
