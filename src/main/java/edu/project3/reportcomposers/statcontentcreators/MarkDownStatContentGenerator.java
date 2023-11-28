package edu.project3.reportcomposers.statcontentcreators;

import edu.project3.logstreamextractors.LogRecord;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MarkDownStatContentGenerator extends AbstractAllStatContentGenerator {

    public MarkDownStatContentGenerator(
        String trackingStartTime,
        String trackingEndTime,
        Stream<LogRecord> logStream,
        Stream<String> sourceNames
    ) {
        super(trackingStartTime, trackingEndTime, logStream, sourceNames);
    }

    @Override
    public String generateContent() {
        StringBuilder builder = new StringBuilder();
        buildMarkDownString(builder);
        return builder.toString();
    }

    @Override
    public String getFileName() {
        return "output.md";
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
        builder
            .append(LIST_ELEMENT)
            .append(String.format(FREQUENCY_STAT_TEMPLATE, QUERIED_RESOURCES))
            .append(LINE_SEPARATOR);
        generateMarkDownTableHeader(builder, true, RESOURCE, QUERIED_TIMES);
        List<Map.Entry<String, Integer>> resourcesStat = resourceTracker.getStat();
        for (Map.Entry<String, Integer> entry : resourcesStat) {
            String res = entry.getKey();
            String times = String.valueOf(entry.getValue());
            generateMarkDownTableRow(builder, true, res, times);
        }
        builder.append(LINE_SEPARATOR);
        builder.append(LIST_ELEMENT)
            .append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_RESPONSE_CODES))
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
        builder
            .append(LIST_ELEMENT)
            .append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_REMOTE_ADDRESSES))
            .append(LINE_SEPARATOR);
        generateMarkDownTableHeader(builder, true, ADDRESS, TIMES);
        List<Map.Entry<String, Integer>> remoteAddressStat = remoteAddressTracker.getStat();
        for (Map.Entry<String, Integer> entry : remoteAddressStat) {
            generateMarkDownTableRow(builder, true, entry.getKey(), String.valueOf(entry.getValue()));
        }
        builder.append(LINE_SEPARATOR);
        builder
            .append(LIST_ELEMENT)
            .append(MOST_PREFIX)
            .append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_REQUEST_TYPE))
            .append(LINE_SEPARATOR);
        Map.Entry<String, Integer> requestTypeStat = requestTypeTracker.getStat();
        builder.append(MARKDOWN_TABULATION).append(INNER_LIST_ELEMENT);
        builder
            .append(requestTypeStat.getKey())
            .append(String.format(REPEATING_STRING_TEMPLATE, requestTypeStat.getValue()))
            .append(LINE_SEPARATOR);
    }

    private void generateMarkDownTableHeader(StringBuilder builder, boolean inner, String... headers) {
        if (inner) {
            builder
                .append(MARKDOWN_TABULATION)
                .append(INNER_LIST_ELEMENT);
        }
        builder.append(TABLE_ROW_SEPARATOR);
        for (String header : headers) {
            builder
                .append(header)
                .append(TABLE_ROW_SEPARATOR);
        }
        builder.append(LINE_SEPARATOR);
        if (inner) {
            builder
                .append(MARKDOWN_TABULATION)
                .append(INNER_LIST_ELEMENT);
        }
        builder.append(TABLE_ROW_SEPARATOR);
        for (int i = 0; i < headers.length; i++) {
            builder
                .append(LEFT_MARGIN)
                .append(TABLE_ROW_SEPARATOR);
        }
        builder.append(LINE_SEPARATOR);
    }

    private void generateMarkDownTableRow(StringBuilder builder, boolean inner, String... values) {
        if (inner) {
            builder
                .append(MARKDOWN_TABULATION)
                .append(INNER_LIST_ELEMENT);
        }
        builder.append(TABLE_ROW_SEPARATOR);
        for (String value : values) {
            builder
                .append(value)
                .append(TABLE_ROW_SEPARATOR);
        }
        builder.append(LINE_SEPARATOR);
    }
}
