package edu.project3.reportcomposers.statcontentcreators;

import edu.project3.logstreamextractors.LogRecord;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TxtStatContentGenerator extends AbstractAllStatContentGenerator {

    public TxtStatContentGenerator(
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
        buildTxtString(builder);
        return builder.toString();
    }

    @Override
    public String getFileName() {
        return "output.txt";
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
        sourceNames.forEach(sourceName ->
            builder
                .append(sourceName)
                .append(LINE_SEPARATOR));
        builder.append(DATE_FROM).append(": ").append(from).append(LINE_SEPARATOR);
        builder.append(DATE_TO).append(": ").append(to).append(LINE_SEPARATOR);
        builder.append(TOTAL_REQUESTS).append(": ").append(totalRequestCounter.getStat()).append(LINE_SEPARATOR);
        builder
            .append(AVERAGE_TRANSFERRED_BYTES)
            .append(": ")
            .append(avgTransferredBytes.getStat())
            .append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR);
    }

    private void generateTxtAdditionalInfo(StringBuilder builder) {
        builder.append("Additional Info:").append(LINE_SEPARATOR);
        builder.append(String.format(FREQUENCY_STAT_TEMPLATE, QUERIED_RESOURCES)).append(LINE_SEPARATOR);
        List<Map.Entry<String, Integer>> resourcesStat = resourceTracker.getStat();
        resourcesStat.forEach(entry ->
            builder
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

}
