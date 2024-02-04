package edu.project3.statextractors;

import edu.project3.logstreamextractors.LogRecord;
import java.util.HashMap;
import java.util.Map;

public class MostFrequentlyRepeatedRequestTypeStatExtractor implements StatExtractor<Map.Entry<String, Integer>> {

    private final Map<String, Integer> frequencyMap = new HashMap<>();

    @Override
    public Map.Entry<String, Integer> getStat() {
        return frequencyMap
            .entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .orElse(Map.entry("N/A", 0));
    }

    @Override
    public void accept(LogRecord logRecord) {
        int currentFreq = frequencyMap.getOrDefault(logRecord.requestType().name(), 0);
        frequencyMap.put(logRecord.requestType().name(), ++currentFreq);
    }
}
