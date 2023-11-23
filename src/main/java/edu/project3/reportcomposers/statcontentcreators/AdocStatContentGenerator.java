package edu.project3.reportcomposers.statcontentcreators;

import edu.project3.logstreamextractors.LogRecord;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class AdocStatContentGenerator extends AbstractAllStatContentGenerator {

    public AdocStatContentGenerator(
        String trackingStartTime,
        String trackingEndTime,
        Stream<LogRecord> logStream,
        Stream<String> sourceNames
    ) {
        super(trackingStartTime, trackingEndTime, logStream, sourceNames);
    }

    @Override
    public String generateContent() {
        StringBuilder builder = new StringBuilder(0);
        buildAdocString(builder);
        return builder.toString();
    }

    @Override
    public String getFileName() {
        return "output.adoc";
    }

    private void buildAdocString(StringBuilder builder) {
        generateAdocGeneralInfo(builder);
        builder.append(LINE_SEPARATOR);
        generateAdocAdditionalInfo(builder);
    }

    private void generateAdocGeneralInfo(StringBuilder builder) {
        builder
            .append("== General Info")
            .append(LINE_SEPARATOR);
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
        builder
            .append("=== Additional Info")
            .append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR);
        builder
            .append(LIST_ELEMENT)
            .append(String.format(FREQUENCY_STAT_TEMPLATE, QUERIED_RESOURCES))
            .append(LINE_SEPARATOR);
        builder
            .append(HARDLINE_BREAK)
            .append(LINE_SEPARATOR);
        generateAdocTableHeader(builder, TWO_COLUMNS, RESOURCE, QUERIED_TIMES);
        List<Map.Entry<String, Integer>> resourcesStat = resourceTracker.getStat();
        for (int i = 0; i < resourcesStat.size(); i++) {
            String res = resourcesStat.get(i).getKey();
            String times = String.valueOf(resourcesStat.get(i).getValue());
            generateAdocTableRow(builder, (i == resourcesStat.size() - 1), res, times);
        }
        builder.append(LINE_SEPARATOR);
        builder
            .append(LIST_ELEMENT)
            .append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_RESPONSE_CODES))
            .append(LINE_SEPARATOR);
        builder
            .append(HARDLINE_BREAK)
            .append(LINE_SEPARATOR);
        generateAdocTableHeader(builder, THREE_COLUMNS, CODE, DESCRIPTION, TIMES);
        List<Map.Entry<Map.Entry<String, Integer>, String>> responseCodes = responseCodesTracker.getStat();
        for (int i = 0; i < responseCodes.size(); i++) {
            String code = responseCodes.get(i).getKey().getKey();
            String desc = responseCodes.get(i).getValue();
            String times = String.valueOf(responseCodes.get(i).getKey().getValue());
            generateAdocTableRow(builder, (i == responseCodes.size() - 1), code, desc, times);
        }
        builder.append(LINE_SEPARATOR);
        builder
            .append(LIST_ELEMENT)
            .append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_REMOTE_ADDRESSES))
            .append(LINE_SEPARATOR);
        builder
            .append(HARDLINE_BREAK)
            .append(LINE_SEPARATOR);
        generateAdocTableHeader(builder, TWO_COLUMNS, ADDRESS, TIMES);
        List<Map.Entry<String, Integer>> remoteAddressStat = remoteAddressTracker.getStat();
        for (int i = 0; i < remoteAddressStat.size(); i++) {
            String address = remoteAddressStat.get(i).getKey();
            String times = String.valueOf(remoteAddressStat.get(i).getValue());
            generateAdocTableRow(builder, (i == remoteAddressStat.size() - 1), address, times);
        }
        builder.append(LINE_SEPARATOR);
        builder
            .append(LIST_ELEMENT)
            .append(MOST_PREFIX)
            .append(String.format(FREQUENCY_STAT_TEMPLATE, REPEATED_REQUEST_TYPE))
            .append(LINE_SEPARATOR);
        builder
            .append(HARDLINE_BREAK)
            .append(LINE_SEPARATOR);
        Map.Entry<String, Integer> requestTypeStat = requestTypeTracker.getStat();
        builder
            .append(requestTypeStat.getKey())
            .append(String.format(REPEATING_STRING_TEMPLATE, requestTypeStat.getValue()))
            .append(LINE_SEPARATOR);
    }

    private void generateAdocTableHeader(StringBuilder builder, int columns, String... headers) {
        builder
            .append("[%header,cols=")
            .append(columns)
            .append("*]")
            .append(LINE_SEPARATOR);
        builder
            .append(ADOC_TABLE_SIGN)
            .append(LINE_SEPARATOR);
        for (String header : headers) {
            builder
                .append(TABLE_ROW_SEPARATOR)
                .append(header)
                .append(LINE_SEPARATOR);
        }
    }

    private void generateAdocTableRow(StringBuilder builder, boolean isEndingRow, String... values) {
        builder.append(LINE_SEPARATOR);
        for (String value : values) {
            builder
                .append(TABLE_ROW_SEPARATOR)
                .append(value)
                .append(LINE_SEPARATOR);
        }
        if (isEndingRow) {
            builder
                .append(ADOC_TABLE_SIGN)
                .append(LINE_SEPARATOR);
        }
    }

}
