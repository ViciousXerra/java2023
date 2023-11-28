package edu.project3.statextractors;

import edu.project3.logstreamextractors.LogRecord;

public class TotalRequestCountStatExtractor implements StatExtractor<Long> {

    private long total;

    @Override
    public Long getStat() {
        return total;
    }

    @Override
    public void accept(LogRecord logRecord) {
        total++;
    }
}
