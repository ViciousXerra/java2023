package edu.project3.reportcomposers;

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

    //Templates and signs
    private final static String LINE_SEPARATOR = System.lineSeparator();
    private final static String FREQUENCY_STAT_TEMPLATE = "Frequently %s: ";
    private final static String QUERIED_RESOURCES = "queried resources";
    private final static String REPEATED_RESPONSE_CODES = "repeated response codes";
    private final static String REPEATED_REMOTE_ADDRESSES = "repeated remote addresses";
    private final static String REPEATED_REQUEST_TYPE = "repeated request type";
    private final static String REPEATING_STRING_TEMPLATE = " repeats %d times.";
    private final static String MOST_PREFIX = "Most ";
    private final static String METRICS = "Metrics";
    private final static String VALUES = "Values";
    private final static String DATE_FROM = "Date, from";
    private final static String DATE_TO = "Date, to";
    private final static String TOTAL_REQUESTS = "Total requests";
    private final static String AVERAGE_TRANSFERRED_BYTES = "Average transferred bytes";
    private final static String RESOURCE = "Resource";
    private final static String QUERIED_TIMES = "Queried, times";
    private final static String CODE = "Code";
    private final static String DESCRIPTION = "Description";
    private final static String TIMES = "Repeated, times";
    private final static String ADDRESS = "Address";

    //Templates for StringBuilder
    private final static String MARKDOWN_TABULATION = "    ";
    private final static String LEFT_MARGIN = ":---";
    private final static String INNER_LIST_ELEMENT = ">";
    private final static String ADOC_TABLE_SIGN = "|===";
    private final static String TABLE_ROW_SEPARATOR = "|";
    private final static String HARDLINE_BREAK = "+";
    private final static String LIST_ELEMENT = "* ";
    private final static int THREE_COLUMNS = 3;
    private final static int TWO_COLUMNS = 2;

    private final String from;
    private final String to;
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
            LOGGER.info("See " + Path.of(directoryToSave, getFileName()) + " for results.");
        } catch (IOException e) {
            LOGGER.error(String.format("Unable to save output. Caught exception: {%s}", e.getMessage()));
        }
    }

    private void collectStat() {
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
            case DEFAULT -> buildTxtString(builder);
            case MARKDOWN -> buildMarkDownString(builder);
            case ADOC -> buildAdocString(builder);
            default -> {
            }
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
        sourceNames.forEach(sourceName -> builder
            .append(sourceName)
            .append(LINE_SEPARATOR));
        builder.append(DATE_FROM).append(": ").append(from).append(LINE_SEPARATOR);
        builder.append(DATE_TO).append(": ").append(to).append(LINE_SEPARATOR);
        builder.append(TOTAL_REQUESTS).append(": ").append(totalRequestCounter.getStat()).append(LINE_SEPARATOR);
        builder.append(AVERAGE_TRANSFERRED_BYTES).append(": ").append(avgTransferredBytes.getStat())
            .append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR);
    }

    private void generateTxtAdditionalInfo(StringBuilder builder) {
        builder.append("Additional Info:").append(LINE_SEPARATOR);
        builder.append(String.format(FREQUENCY_STAT_TEMPLATE, QUERIED_RESOURCES)).append(LINE_SEPARATOR);
        List<Map.Entry<String, Integer>> resourcesStat = resourceTracker.getStat();
        resourcesStat.forEach(entry -> builder
            .append(entry.getKey())
            .append(String.format(" queried %d times.", entry.getValue()))
            .append(LINE_SEPARATOR)
        );
        builder.append(LINE_SEPARATOR);
        builder.append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_REMOTE_ADDRESSES)).append(LINE_SEPARATOR);
        List<Map.Entry<String, Integer>> remoteAddressStat = remoteAddressTracker.getStat();
        remoteAddressStat.forEach(entry ->
            builder
                .append(entry.getKey())
                .append(String.format(REPEATING_STRING_TEMPLATE, entry.getValue()))
                .append(LINE_SEPARATOR)
        );
        builder.append(LINE_SEPARATOR);
        builder.append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_RESPONSE_CODES)).append(LINE_SEPARATOR);
        List<Map.Entry<Map.Entry<String, Integer>, String>> responseCodes = responseCodesTracker.getStat();
        responseCodes.forEach(entry -> builder
            .append(entry.getKey().getKey()).append(" ").append(entry.getValue())
            .append(String.format(REPEATING_STRING_TEMPLATE, entry.getKey().getValue()))
            .append(LINE_SEPARATOR)
        );
        builder.append(LINE_SEPARATOR);
        builder
            .append(String.format(MOST_PREFIX + FREQUENCY_STAT_TEMPLATE, REPEATED_REQUEST_TYPE))
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
        builder.append("## General Info").append(LINE_SEPARATOR);
        generateMarkDownTableHeader(builder, false, METRICS, VALUES);
        sourceNames.forEach(name -> generateMarkDownTableRow(builder, false, "Source", name));
        generateMarkDownTableRow(builder, false, DATE_FROM, from);
        generateMarkDownTableRow(builder, false, DATE_TO, to);
        generateMarkDownTableRow(builder, false,
            TOTAL_REQUESTS, String.valueOf(totalRequestCounter.getStat())

        );
        generateMarkDownTableRow(builder, false,
            AVERAGE_TRANSFERRED_BYTES, String.valueOf(avgTransferredBytes.getStat())
        );
    }

    private void generateMarkDownAdditionalInfo(StringBuilder builder) {
        builder.append("### Additional Info").append(LINE_SEPARATOR);
        builder.append(LIST_ELEMENT).append(String.format(FREQUENCY_STAT_TEMPLATE, QUERIED_RESOURCES))
            .append(LINE_SEPARATOR);
        generateMarkDownTableHeader(builder, true, RESOURCE, QUERIED_TIMES);
        List<Map.Entry<String, Integer>> resourcesStat = resourceTracker.getStat();
        for (Map.Entry<String, Integer> entry : resourcesStat) {
            String res = entry.getKey();
            String times = String.valueOf(entry.getValue());
            generateMarkDownTableRow(builder, true, res, times);
        }
        builder.append(LINE_SEPARATOR);
        builder.append(LIST_ELEMENT).append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_RESPONSE_CODES))
            .append(LINE_SEPARATOR);
        generateMarkDownTableHeader(builder, true, CODE, DESCRIPTION, TIMES);
        List<Map.Entry<Map.Entry<String, Integer>, String>> responseCodes = responseCodesTracker.getStat();
        for (Map.Entry<Map.Entry<String, Integer>, String> stringIntegerEntry : responseCodes) {
            String code = stringIntegerEntry.getKey().getKey();
            String desc = stringIntegerEntry.getValue();
            String times = String.valueOf(stringIntegerEntry.getKey().getValue());
            generateMarkDownTableRow(builder, true, code, desc, times);
        }
        builder.append(LINE_SEPARATOR);
        builder.append(LIST_ELEMENT).append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_REMOTE_ADDRESSES))
            .append(LINE_SEPARATOR);
        generateMarkDownTableHeader(builder, true, ADDRESS, TIMES);
        List<Map.Entry<String, Integer>> remoteAddressStat = remoteAddressTracker.getStat();
        for (Map.Entry<String, Integer> entry : remoteAddressStat) {
            generateMarkDownTableRow(builder, true, entry.getKey(), String.valueOf(entry.getValue()));
        }
        builder.append(LINE_SEPARATOR);
        builder.append(LIST_ELEMENT).append(MOST_PREFIX)
            .append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_REQUEST_TYPE))
            .append(LINE_SEPARATOR);
        Map.Entry<String, Integer> requestTypeStat = requestTypeTracker.getStat();
        builder.append(MARKDOWN_TABULATION).append(INNER_LIST_ELEMENT);
        builder.append(requestTypeStat.getKey())
            .append(String.format(REPEATING_STRING_TEMPLATE, requestTypeStat.getValue())).append(LINE_SEPARATOR);
    }

    private void generateMarkDownTableHeader(StringBuilder builder, boolean inner, String... headers) {
        if (inner) {
            builder.append(MARKDOWN_TABULATION).append(INNER_LIST_ELEMENT);
        }
        builder.append(TABLE_ROW_SEPARATOR);
        for (String header : headers) {
            builder.append(header).append(TABLE_ROW_SEPARATOR);
        }
        builder.append(LINE_SEPARATOR);
        if (inner) {
            builder.append(MARKDOWN_TABULATION).append(INNER_LIST_ELEMENT);
        }
        builder.append(TABLE_ROW_SEPARATOR);
        for (int i = 0; i < headers.length; i++) {
            builder.append(LEFT_MARGIN).append(TABLE_ROW_SEPARATOR);
        }
        builder.append(LINE_SEPARATOR);
    }

    private void generateMarkDownTableRow(StringBuilder builder, boolean inner, String... values) {
        if (inner) {
            builder.append(MARKDOWN_TABULATION).append(INNER_LIST_ELEMENT);
        }
        builder.append(TABLE_ROW_SEPARATOR);
        for (String value : values) {
            builder.append(value).append(TABLE_ROW_SEPARATOR);
        }
        builder.append(LINE_SEPARATOR);
    }

    private void buildAdocString(StringBuilder builder) {
        generateAdocGeneralInfo(builder);
        builder.append(LINE_SEPARATOR);
        generateAdocAdditionalInfo(builder);
    }

    private void generateAdocGeneralInfo(StringBuilder builder) {
        builder.append("== General Info").append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR);
        generateAdocTableHeader(builder, TWO_COLUMNS, METRICS, VALUES);
        StringBuilder names = new StringBuilder();
        sourceNames.forEach(name -> names.append(name).append(" +").append(LINE_SEPARATOR));
        generateAdocTableRow(builder, false, "Sources", names.toString());
        generateAdocTableRow(builder, false, DATE_FROM, from);
        generateAdocTableRow(builder, false, DATE_TO, to);
        generateAdocTableRow(
            builder, false,
            TOTAL_REQUESTS, String.valueOf(totalRequestCounter.getStat())
        );
        generateAdocTableRow(
            builder, true,
            AVERAGE_TRANSFERRED_BYTES, String.valueOf(avgTransferredBytes.getStat())
        );
    }

    private void generateAdocAdditionalInfo(StringBuilder builder) {
        builder.append("=== Additional Info").append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR);
        builder.append(LIST_ELEMENT).append(String.format(FREQUENCY_STAT_TEMPLATE, QUERIED_RESOURCES))
            .append(LINE_SEPARATOR);
        builder.append(HARDLINE_BREAK).append(LINE_SEPARATOR);
        generateAdocTableHeader(builder, TWO_COLUMNS, RESOURCE, QUERIED_TIMES);
        List<Map.Entry<String, Integer>> resourcesStat = resourceTracker.getStat();
        for (int i = 0; i < resourcesStat.size(); i++) {
            String res = resourcesStat.get(i).getKey();
            String times = String.valueOf(resourcesStat.get(i).getValue());
            generateAdocTableRow(builder, (i == resourcesStat.size() - 1), res, times);
        }
        builder.append(LINE_SEPARATOR);
        builder.append(LIST_ELEMENT).append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_RESPONSE_CODES))
            .append(LINE_SEPARATOR);
        builder.append(HARDLINE_BREAK).append(LINE_SEPARATOR);
        generateAdocTableHeader(builder, THREE_COLUMNS, CODE, DESCRIPTION, TIMES);
        List<Map.Entry<Map.Entry<String, Integer>, String>> responseCodes = responseCodesTracker.getStat();
        for (int i = 0; i < responseCodes.size(); i++) {
            String code = responseCodes.get(i).getKey().getKey();
            String desc = responseCodes.get(i).getValue();
            String times = String.valueOf(responseCodes.get(i).getKey().getValue());
            generateAdocTableRow(builder, (i == responseCodes.size() - 1), code, desc, times);
        }
        builder.append(LINE_SEPARATOR);
        builder.append(LIST_ELEMENT).append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_REMOTE_ADDRESSES))
            .append(LINE_SEPARATOR);
        builder.append(HARDLINE_BREAK).append(LINE_SEPARATOR);
        generateAdocTableHeader(builder, TWO_COLUMNS, ADDRESS, TIMES);
        List<Map.Entry<String, Integer>> remoteAddressStat = remoteAddressTracker.getStat();
        for (int i = 0; i < remoteAddressStat.size(); i++) {
            String address = remoteAddressStat.get(i).getKey();
            String times = String.valueOf(remoteAddressStat.get(i).getValue());
            generateAdocTableRow(builder, (i == remoteAddressStat.size() - 1), address, times);
        }
        builder.append(LINE_SEPARATOR);
        builder.append(LIST_ELEMENT).append(MOST_PREFIX)
            .append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_REQUEST_TYPE))
            .append(LINE_SEPARATOR);
        builder.append(HARDLINE_BREAK).append(LINE_SEPARATOR);
        Map.Entry<String, Integer> requestTypeStat = requestTypeTracker.getStat();
        builder.append(requestTypeStat.getKey())
            .append(String.format(REPEATING_STRING_TEMPLATE, requestTypeStat.getValue())).append(LINE_SEPARATOR);
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

    public enum ReportType {
        DEFAULT,
        MARKDOWN,
        ADOC
    }

}
