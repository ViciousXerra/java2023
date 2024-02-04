package edu.project3.logstreamextractors;

import java.util.stream.Stream;

public interface LogStreamExtractor {

    Stream<LogRecord> extract();

    String[] getSourceName();

}
