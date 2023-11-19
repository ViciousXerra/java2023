package edu.project3.statextractors;

import edu.project3.logstreamextractors.LogRecord;

public class AverageTransferredBytesStatExtractor implements StatExtractor<Long> {

    private long totalBytes;
    private long totalCount;

    @Override
    public Long getStat() {
        return totalBytes / totalCount;
    }

    @Override
    public void accept(LogRecord logRecord) {
        if (logRecord.transferredBytes() > 0) {
            totalBytes += logRecord.transferredBytes();
            totalCount++;
        }
    }
}
