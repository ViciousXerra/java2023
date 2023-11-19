package edu.project3.statextractors;

import edu.project3.logstreamextractors.LogRecord;
import java.util.function.Consumer;

public interface StatExtractor<T> extends Consumer<LogRecord> {

    T getStat();

}
