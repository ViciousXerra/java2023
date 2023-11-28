package edu.project3.reportcomposers.statcontentcreators;

import edu.project3.logstreamextractors.LogRecord;
import edu.project3.statextractors.AverageTransferredBytesStatExtractor;
import edu.project3.statextractors.FrequentlyQueriedResourcesStatExtractor;
import edu.project3.statextractors.FrequentlyRepeatedRemoteAddressStatExtractor;
import edu.project3.statextractors.MostFrequentlyRepeatedRequestTypeStatExtractor;
import edu.project3.statextractors.ResponseCodesCountStatExtractor;
import edu.project3.statextractors.StatExtractor;
import edu.project3.statextractors.TotalRequestCountStatExtractor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

abstract class AbstractAllStatContentGenerator implements StatContentCreator {

    //Templates and signs
    protected final static String LINE_SEPARATOR = System.lineSeparator();
    protected final static String FREQUENCY_STAT_TEMPLATE = "Frequently %s: ";
    protected final static String QUERIED_RESOURCES = "queried resources";
    protected final static String REPEATED_RESPONSE_CODES = "repeated response codes";
    protected final static String REPEATED_REMOTE_ADDRESSES = "repeated remote addresses";
    protected final static String REPEATED_REQUEST_TYPE = "repeated request type";
    protected final static String REPEATING_STRING_TEMPLATE = " repeats %d times.";
    protected final static String MOST_PREFIX = "Most ";
    protected final static String METRICS = "Metrics";
    protected final static String VALUES = "Values";
    protected final static String DATE_FROM = "Date, from";
    protected final static String DATE_TO = "Date, to";
    protected final static String TOTAL_REQUESTS = "Total requests";
    protected final static String AVERAGE_TRANSFERRED_BYTES = "Average transferred bytes";
    protected final static String RESOURCE = "Resource";
    protected final static String QUERIED_TIMES = "Queried, times";
    protected final static String CODE = "Code";
    protected final static String DESCRIPTION = "Description";
    protected final static String TIMES = "Repeated, times";
    protected final static String ADDRESS = "Address";

    //Templates for StringBuilder
    protected final static String MARKDOWN_TABULATION = "    ";
    protected final static String LEFT_MARGIN = ":---";
    protected final static String INNER_LIST_ELEMENT = ">";
    protected final static String ADOC_TABLE_SIGN = "|===";
    protected final static String TABLE_ROW_SEPARATOR = "|";
    protected final static String HARDLINE_BREAK = "+";
    protected final static String LIST_ELEMENT = "* ";
    protected final static int THREE_COLUMNS = 3;
    protected final static int TWO_COLUMNS = 2;

    protected final StatExtractor<Long> totalRequestCounter = new TotalRequestCountStatExtractor();
    protected final StatExtractor<Long> avgTransferredBytes = new AverageTransferredBytesStatExtractor();
    protected final StatExtractor<List<Map.Entry<String, Integer>>> resourceTracker =
        new FrequentlyQueriedResourcesStatExtractor();
    protected final StatExtractor<List<Map.Entry<String, Integer>>> remoteAddressTracker =
        new FrequentlyRepeatedRemoteAddressStatExtractor();
    protected final StatExtractor<Map.Entry<String, Integer>> requestTypeTracker =
        new MostFrequentlyRepeatedRequestTypeStatExtractor();
    protected final StatExtractor<List<Map.Entry<Map.Entry<String, Integer>, String>>> responseCodesTracker =
        new ResponseCodesCountStatExtractor();

    private final List<Consumer<LogRecord>> statCollectors = new ArrayList<>() {
        {
            add(totalRequestCounter);
            add(avgTransferredBytes);
            add(resourceTracker);
            add(remoteAddressTracker);
            add(requestTypeTracker);
            add(responseCodesTracker);
        }
    };

    protected final String from;
    protected final String to;
    protected final Stream<String> sourceNames;
    private final Stream<LogRecord> logStream;

    protected AbstractAllStatContentGenerator(
        String trackingStartTime,
        String trackingEndTime,
        Stream<LogRecord> logStream,
        Stream<String> sourceNames
    ) {
        this.from = trackingStartTime == null ? "-" : OffsetDateTime.of(
            LocalDate.parse(trackingStartTime, DateTimeFormatter.ISO_LOCAL_DATE),
            LocalTime.MIDNIGHT,
            ZoneOffset.UTC
        ).toString();
        this.to = trackingEndTime == null ? "-" : OffsetDateTime.of(
            LocalDate.parse(trackingEndTime, DateTimeFormatter.ISO_LOCAL_DATE),
            LocalTime.MIDNIGHT,
            ZoneOffset.UTC
        ).toString();
        this.logStream = logStream;
        this.sourceNames = sourceNames;
    }

    @Override
    public final void collectStat() {
        logStream.forEach(logRecord ->
            statCollectors.forEach(logRecordConsumer -> logRecordConsumer.accept(logRecord)));
    }

}
