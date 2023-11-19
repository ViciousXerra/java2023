package edu.project3.statextractors;

import edu.project3.logstreamextractors.LogRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseCodesCountStatExtractor
    implements StatExtractor<List<Map.Entry<Map.Entry<String, Integer>, String>>> {

    private final static Map<String, String> COMMON_RESPONSE_CODES = new HashMap<>() {
        {
            put("200", "Success/OK");
            put("201", "Created");
            put("202", "Accepted");
            put("203", "Non-Authoritative Information");
            put("204", "No Content");
            put("301", "Permanent Redirect");
            put("302", "Temporary Redirect");
            put("304", "Not Modified");
            put("400", "Bad Request");
            put("401", "Unauthorized Error");
            put("403", "Forbidden");
            put("404", "Not Found");
            put("500", "Internal Server Error");
            put("501", "Not implemented");
        }
    };

    private final Map<String, Integer> frequencyMap = new HashMap<>();

    @Override
    public List<Map.Entry<Map.Entry<String, Integer>, String>> getStat() {
        return frequencyMap
            .entrySet()
            .stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .map(entry -> {
                String description = COMMON_RESPONSE_CODES.getOrDefault(entry.getKey(), "N/A");
                return Map.entry(entry, description);
            })
            .toList();
    }

    @Override
    public void accept(LogRecord logRecord) {
        int currentFreq = frequencyMap.getOrDefault(logRecord.status(), 0);
        frequencyMap.put(logRecord.status(), ++currentFreq);
    }
}
