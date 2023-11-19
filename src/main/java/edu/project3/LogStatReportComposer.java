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
    private final static String QUERIED_RESOURCES = "queried resources";
    private final static String REPEATED_RESPONSE_CODES = "repeated response codes";
    private final static String REPEATED_REMOTE_ADDRESSES = "repeated remote addresses";
    private final static String REPEATED_REQUEST_TYPE = "repeated request type";
    private final static String TIMES = "Repeated, times";
    private final static String REPEATING_STRING_TEMPLATE = " repeats %d times.";
    private final static String ADOC_TABLE_SIGN = "|===";
    private final static String TABLE_ROW_SEPARATOR = "|";

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

    public String getFormattedString() {
        StringBuilder builder = new StringBuilder();
        switch (format) {
            case DEFAULT -> buildTxtString(builder);
            case MARKDOWN -> buildMarkDownString(builder);
            case ADOC -> buildAdocString(builder);
        }
        return builder.toString();
    }

    private void buildTxtString(StringBuilder builder) {
        generateTxtGeneralInfo(builder);
        builder.append(LINE_SEPARATOR);
        generateTxtAdditionalInfo(builder);
    }

    private void generateTxtGeneralInfo(StringBuilder builder) {
        builder
            .append("General Info:")
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
    }

    private void generateTxtAdditionalInfo(StringBuilder builder) {
        builder.append("Additional Info:").append(LINE_SEPARATOR);
        builder.append(String.format(FREQUENCY_STAT_TEMPLATE, QUERIED_RESOURCES)).append(LINE_SEPARATOR);
        List<Map.Entry<String, Integer>> resourcesStat = resourceTracker.getStat();
        resourcesStat.forEach(entry -> {
            builder
                .append(entry.getKey())
                .append(String.format(" queried %d times.", entry.getValue()))
                .append(LINE_SEPARATOR);
        });
        builder.append(LINE_SEPARATOR);
        builder.append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_REMOTE_ADDRESSES)).append(LINE_SEPARATOR);
        List<Map.Entry<String, Integer>> remoteAddressStat = remoteAddressTracker.getStat();
        remoteAddressStat.forEach(entry ->
            builder
                .append(entry.getKey())
                .append(String.format(REPEATING_STRING_TEMPLATE, entry.getValue()))
                .append(LINE_SEPARATOR));
        builder.append(LINE_SEPARATOR);
        builder.append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_RESPONSE_CODES)).append(LINE_SEPARATOR);
        List<Map.Entry<Map.Entry<String, Integer>, String>> responseCodes = responseCodesTracker.getStat();
        responseCodes.forEach(entry -> {
            builder
                .append(entry.getKey().getKey()).append(" ").append(entry.getValue())
                .append(String.format(REPEATING_STRING_TEMPLATE, entry.getKey().getValue()))
                .append(LINE_SEPARATOR);
        });
        builder.append(LINE_SEPARATOR);
        builder
            .append(String.format("Most " + FREQUENCY_STAT_TEMPLATE, REPEATED_REQUEST_TYPE))
            .append(LINE_SEPARATOR);
        Map.Entry<String, Integer> requestTypeStat = requestTypeTracker.getStat();
        builder
            .append(requestTypeStat.getKey())
            .append(String.format(REPEATING_STRING_TEMPLATE, requestTypeStat.getValue()))
            .append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR);
    }

    private void buildMarkDownString(StringBuilder builder) {
        generateMarkDownGeneralInfo(builder);
        builder.append(LINE_SEPARATOR);
        generateMarkDownAdditionalInfo(builder);
    }

    private void generateMarkDownGeneralInfo(StringBuilder builder) {

    }

    private void generateMarkDownAdditionalInfo(StringBuilder builder) {

    }

    private void buildAdocString(StringBuilder builder) {
        generateAdocGeneralInfo(builder);
        builder.append(LINE_SEPARATOR);
        generateAdocAdditionalInfo(builder);
    }

    private void generateAdocGeneralInfo(StringBuilder builder) {
        builder.append("== General Info").append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR);
        generateAdocTableHeader(builder, 2, "Metrics", "Values");
        StringBuilder names = new StringBuilder();
        sourceNames.forEach(name -> names.append(name).append(" +").append(LINE_SEPARATOR));
        generateAdocTableRow(builder, false, "Sources", names.toString());
        generateAdocTableRow(builder, false, "Date, from", from.toString());
        generateAdocTableRow(builder, false, "Date, to", to.toString());
        generateAdocTableRow(
            builder, false,
            "Total requests", String.valueOf(totalRequestCounter.getStat())
        );
        generateAdocTableRow(
            builder, true,
            "Average transferred bytes", String.valueOf(avgTransferredBytes.getStat())
        );
    }

    private void generateAdocTableHeader(StringBuilder builder, int columns, String... headers) {
        builder.append("[%header,cols=").append(columns).append("*]").append(LINE_SEPARATOR);
        builder.append(ADOC_TABLE_SIGN).append(LINE_SEPARATOR);
        for (String header : headers) {
            builder.append(TABLE_ROW_SEPARATOR).append(header).append(LINE_SEPARATOR);
        }
    }

    private void generateAdocTableRow(StringBuilder builder, boolean isEndingRow, String... values) {
        builder.append(LINE_SEPARATOR);
        for (String value : values) {
            builder.append(TABLE_ROW_SEPARATOR).append(value).append(LINE_SEPARATOR);
        }
        if (isEndingRow) {
            builder.append(ADOC_TABLE_SIGN).append(LINE_SEPARATOR);
        }
    }

    private void generateAdocAdditionalInfo(StringBuilder builder) {
        builder.append("=== Additional Info").append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR);
        builder.append("* ").append(String.format(FREQUENCY_STAT_TEMPLATE, QUERIED_RESOURCES)).append(LINE_SEPARATOR);
        builder.append("+").append(LINE_SEPARATOR);
        generateAdocTableHeader(builder, 2, "Resource", "Queried, times");
        List<Map.Entry<String, Integer>> resourcesStat = resourceTracker.getStat();
        for (int i = 0; i < resourcesStat.size(); i++) {
            String res = resourcesStat.get(i).getKey();
            String times = String.valueOf(resourcesStat.get(i).getValue());
            generateAdocTableRow(builder, (i == resourcesStat.size() - 1), res, times);
        }
        builder.append(LINE_SEPARATOR);
        builder.append("* ").append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_RESPONSE_CODES))
            .append(LINE_SEPARATOR);
        builder.append("+").append(LINE_SEPARATOR);
        generateAdocTableHeader(builder, 3, "Code", "Description", TIMES);
        List<Map.Entry<Map.Entry<String, Integer>, String>> responseCodes = responseCodesTracker.getStat();
        for (int i = 0; i < responseCodes.size(); i++) {
            String code = responseCodes.get(i).getKey().getKey();
            String desc = responseCodes.get(i).getValue();
            String times = String.valueOf(responseCodes.get(i).getKey().getValue());
            generateAdocTableRow(builder, (i == responseCodes.size() - 1), code, desc, times);
        }
        builder.append(LINE_SEPARATOR);
        builder.append("* ").append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_REMOTE_ADDRESSES))
            .append(LINE_SEPARATOR);
        builder.append("+").append(LINE_SEPARATOR);
        generateAdocTableHeader(builder, 2, "Address", TIMES);
        List<Map.Entry<String, Integer>> remoteAddressStat = remoteAddressTracker.getStat();
        for (int i = 0; i < remoteAddressStat.size(); i++) {
            String address = remoteAddressStat.get(i).getKey();
            String times = String.valueOf(remoteAddressStat.get(i).getValue());
            generateAdocTableRow(builder, (i == remoteAddressStat.size() - 1), address, times);
        }
        builder.append(LINE_SEPARATOR);
        builder.append("* ").append("Most ").append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_REQUEST_TYPE))
            .append(LINE_SEPARATOR);
        builder.append("+").append(LINE_SEPARATOR);
        Map.Entry<String, Integer> requestTypeStat = requestTypeTracker.getStat();
        builder.append(requestTypeStat.getKey())
            .append(String.format(REPEATING_STRING_TEMPLATE, requestTypeStat.getValue())).append(LINE_SEPARATOR);
    }

    public enum ReportType {
        DEFAULT,
        MARKDOWN,
        ADOC
    }

}
