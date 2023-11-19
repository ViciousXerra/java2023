package edu.project3;

import edu.project3.logstreamextractors.LogRecord;
import edu.project3.statextractors.AverageTransferredBytesStatExtractor;
import edu.project3.statextractors.FrequentlyQueriedResourcesStatExtractor;
import edu.project3.statextractors.FrequentlyRepeatedRemoteAddressStatExtractor;
import edu.project3.statextractors.MostFrequentlyRepeatedRequestTypeStatExtractor;
import edu.project3.statextractors.ResponseCodesCountStatExtractor;
import edu.project3.statextractors.StatExtractor;
import edu.project3.statextractors.TotalRequestCountStatExtractor;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogStatReportComposer implements StatReportComposer {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String LINE_SEPARATOR = System.lineSeparator();
    private final static String FREQUENCY_STAT_TEMPLATE = "Frequently %s: ";
    private final static String REPEATING_STRING_TEMPLATE = " repeats %d times.";

    private final OffsetDateTime from;
    private final OffsetDateTime to;
    private final Stream<LogRecord> logStream;
    private final Stream<String> sourceNames;
    private final ReportType format;
    private final String directoryToSave;

    private final StatExtractor<Long> totalRequestCounter = new TotalRequestCountStatExtractor();
    private final StatExtractor<Long> avgTransferredBytes = new AverageTransferredBytesStatExtractor();
    private final StatExtractor<List<Map.Entry<String, Integer>>> resourceTracker =
        new FrequentlyQueriedResourcesStatExtractor();
    private final StatExtractor<List<Map.Entry<String, Integer>>> remoteAddressTracker =
        new FrequentlyRepeatedRemoteAddressStatExtractor();
    private final StatExtractor<Map.Entry<String, Integer>> requestTypeTracker =
        new MostFrequentlyRepeatedRequestTypeStatExtractor();
    private final StatExtractor<List<Map.Entry<Map.Entry<String, Integer>, String>>> responseCodesTracker =
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

    public LogStatReportComposer(
        String trackingStartTime,
        String trackingEndTime,
        Stream<LogRecord> logStream,
        Stream<String> sourceNames,
        ReportType format,
        String directoryToSave
    ) {
        this.from = OffsetDateTime.of(
            LocalDate.parse(trackingStartTime, DateTimeFormatter.ISO_LOCAL_DATE),
            LocalTime.MIDNIGHT,
            ZoneOffset.UTC
        );
        this.to = OffsetDateTime.of(
            LocalDate.parse(trackingEndTime, DateTimeFormatter.ISO_LOCAL_DATE),
            LocalTime.MIDNIGHT,
            ZoneOffset.UTC
        );
        this.logStream = logStream;
        this.sourceNames = sourceNames;
        this.format = format;
        this.directoryToSave = directoryToSave;
    }

    @Override
    public void createReport() {
        collectStat();
        try (BufferedWriter writer = Files.newBufferedWriter(
            Path.of(directoryToSave, getFileName()),
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.TRUNCATE_EXISTING
        )) {
            String formattedOutput = getFormattedString();
            writer.write(formattedOutput);
        } catch (IOException e) {
            LOGGER.error(String.format("Unable to save output. Caught exception: {%s}", e.getMessage()));
        }
    }

    public void collectStat() {
        logStream.forEach(logRecord ->
            statCollectors.forEach(logRecordConsumer -> logRecordConsumer.accept(logRecord)));
    }

    private String getFileName() {
        String fileName = "output";
        return switch (format) {
            case DEFAULT -> fileName + ".txt";
            case MARKDOWN -> fileName + ".md";
            case ADOC -> fileName + ".adoc";
        };
    }

    private String getFormattedString() {
        StringBuilder builder = new StringBuilder();
        switch (format) {
            case DEFAULT -> getTxtString(builder);
            case MARKDOWN -> getMarkdownString(builder);
            case ADOC -> getAdocString(builder);
        };
    }

    public String getTxtString(StringBuilder builder) {
        builder
            .append("REPORT")
            .append(LINE_SEPARATOR)
            .append("Sources: ")
            .append(LINE_SEPARATOR);
        sourceNames.forEach(sourceName -> {
            builder
                .append(sourceName)
                .append(LINE_SEPARATOR);
        });
        builder.append("From: ").append(from.toString()).append(LINE_SEPARATOR);
        builder.append("To: ").append(to.toString()).append(LINE_SEPARATOR);
        builder.append("Total requests number: ").append(totalRequestCounter.getStat()).append(LINE_SEPARATOR);
        builder.append("Average transferred bytes: ").append(avgTransferredBytes.getStat()).append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR);
        builder.append(String.format(FREQUENCY_STAT_TEMPLATE, "queried resources")).append(LINE_SEPARATOR);
        List<Map.Entry<String, Integer>> resourcesStat = resourceTracker.getStat();
        resourcesStat.forEach(entry -> {
            builder
                .append(entry.getKey())
                .append(String.format(" queried %d times.", entry.getValue()))
                .append(LINE_SEPARATOR);
        });
        builder.append(LINE_SEPARATOR);
        builder.append(String.format(FREQUENCY_STAT_TEMPLATE, "repeated remote addresses")).append(LINE_SEPARATOR);
        List<Map.Entry<String, Integer>> remoteAddressStat = remoteAddressTracker.getStat();
        remoteAddressStat.forEach(entry ->
            builder
                .append(entry.getKey())
                .append(String.format(REPEATING_STRING_TEMPLATE, entry.getValue()))
                .append(LINE_SEPARATOR));
        builder.append(LINE_SEPARATOR);
        builder.append(String.format(FREQUENCY_STAT_TEMPLATE, "repeated response codes")).append(LINE_SEPARATOR);
        List<Map.Entry<Map.Entry<String, Integer>, String>> responseCodes = responseCodesTracker.getStat();
        responseCodes.forEach(entry -> {
            builder
                .append(entry.getKey().getKey()).append(" ").append(entry.getValue())
                .append(String.format(REPEATING_STRING_TEMPLATE, entry.getKey().getValue()))
                .append(LINE_SEPARATOR);
        });
        builder.append(LINE_SEPARATOR);
        builder
            .append(String.format("Most " + FREQUENCY_STAT_TEMPLATE, "repeated request type"))
            .append(LINE_SEPARATOR);
        Map.Entry<String, Integer> requestTypeStat = requestTypeTracker.getStat();
        builder
            .append(requestTypeStat.getKey())
            .append(String.format(REPEATING_STRING_TEMPLATE, requestTypeStat.getValue()))
            .append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR);
        return builder.toString();
    }

    private String getMarkdownString() {

    }

    private String getAdocString() {
        StringBuilder builder = new StringBuilder();
        builder.append("== General Info").append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR);
        builder.append("[%header,cols=2*]").append(LINE_SEPARATOR);
        builder.append("|===").append(LINE_SEPARATOR);
        builder.append("|Metrics").append(LINE_SEPARATOR);
        builder.append("|Values").append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR);
        builder.append("|Sources").append(LINE_SEPARATOR);
        builder.append("|");
        sourceNames.forEach(sourceName -> {
            builder
                .append(sourceName)
                .append(" +")
                .append(LINE_SEPARATOR);
        });
        builder.append(LINE_SEPARATOR);
        builder.append("|Date, from").append(LINE_SEPARATOR);
        builder.append("|").append(from.toString()).append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR);
        builder.append("|Date, to").append(LINE_SEPARATOR);
        builder.append("|").append(to.toString()).append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR);
        builder.append("|Total requests").append(LINE_SEPARATOR);
        builder.append("|").append(totalRequestCounter.getStat()).append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR);
        builder.append("|Average transferred bytes").append(LINE_SEPARATOR);
        builder.append("|").append(avgTransferredBytes.getStat()).append(LINE_SEPARATOR);
        builder.append("|===");

    }

    public enum ReportType {
        DEFAULT,
        MARKDOWN,
        ADOC
    }

}
