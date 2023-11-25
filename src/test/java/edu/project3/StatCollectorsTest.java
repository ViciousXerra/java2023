package edu.project3;

import edu.project3.logstreamextractors.LogRecord;
import edu.project3.logstreamextractors.LogUtils;
import edu.project3.statextractors.AverageTransferredBytesStatExtractor;
import edu.project3.statextractors.FrequentlyQueriedResourcesStatExtractor;
import edu.project3.statextractors.FrequentlyRepeatedRemoteAddressStatExtractor;
import edu.project3.statextractors.MostFrequentlyRepeatedRequestTypeStatExtractor;
import edu.project3.statextractors.ResponseCodesCountStatExtractor;
import edu.project3.statextractors.StatExtractor;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import edu.project3.statextractors.TotalRequestCountStatExtractor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class StatCollectorsTest {

    private static Stream<LogRecord> getPreparedStream() {
        return Stream.of(
            new LogRecord("93.180.71.3", "-",
                OffsetDateTime.parse("17/May/2015:08:05:32 +0000", LogUtils.OFFSET_DATE_TIME_FORMATTER),
                LogRecord.RequestType.GET, "/downloads/product_1",
                "304", 0L, "-", "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)"
            ),
            new LogRecord("217.168.17.5", "-",
                OffsetDateTime.parse("17/May/2015:08:05:34 +0000", LogUtils.OFFSET_DATE_TIME_FORMATTER),
                LogRecord.RequestType.GET, "/downloads/product_1",
                "200", 490L, "-", "Debian APT-HTTP/1.3 (0.8.10.3)"
            ),
            new LogRecord("217.168.17.5", "-",
                OffsetDateTime.parse("17/May/2015:08:05:09 +0000", LogUtils.OFFSET_DATE_TIME_FORMATTER),
                LogRecord.RequestType.GET, "/downloads/product_2",
                "200", 490L, "-", "Debian APT-HTTP/1.3 (0.8.10.3)"
            )
        );
    }

    @Test
    @DisplayName("Test average bytes stat collector.")
    void testAvgBytesStatCollector() {
        //Given
        long expected = 490L;
        //When
        StatExtractor<Long> extractor = new AverageTransferredBytesStatExtractor();
        getPreparedStream().forEach(extractor);
        long actual = extractor.getStat();
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test frequently queried resource.")
    void testFrequentlyQueriedRes() {
        //Given
        List<Map.Entry<String, Integer>> expected = List.of(
            Map.entry("/downloads/product_1", 2),
            Map.entry("/downloads/product_2", 1)
        );
        //When
        StatExtractor<List<Map.Entry<String, Integer>>> extractor = new FrequentlyQueriedResourcesStatExtractor();
        getPreparedStream().forEach(extractor);
        List<Map.Entry<String, Integer>> actual = extractor.getStat();
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test frequently repeated addresses.")
    void testFrequentlyRepeatedAddresses() {
        //Given
        List<Map.Entry<String, Integer>> expected = List.of(
            Map.entry("217.168.17.5", 2),
            Map.entry("93.180.71.3", 1)
        );
        //When
        StatExtractor<List<Map.Entry<String, Integer>>> extractor = new FrequentlyRepeatedRemoteAddressStatExtractor();
        getPreparedStream().forEach(extractor);
        List<Map.Entry<String, Integer>> actual = extractor.getStat();
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test most frequently repeated request type.")
    void testMostFrequentlyRepeatedRequestType() {
        //Given
        Map.Entry<String, Integer> expected = Map.entry("GET", 3);
        //When
        StatExtractor<Map.Entry<String, Integer>> extractor = new MostFrequentlyRepeatedRequestTypeStatExtractor();
        getPreparedStream().forEach(extractor);
        Map.Entry<String, Integer> actual = extractor.getStat();
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test response codes counter.")
    void testResponseCodesCounter() {
        //Given
        List<Map.Entry<Map.Entry<String, Integer>, String>> expected = List.of(
            Map.entry(Map.entry("200", 2), "Success/OK"),
            Map.entry(Map.entry("304", 1), "Not Modified")
        );
        //When
        StatExtractor<List<Map.Entry<Map.Entry<String, Integer>, String>>> extractor =
            new ResponseCodesCountStatExtractor();
        getPreparedStream().forEach(extractor);
        List<Map.Entry<Map.Entry<String, Integer>, String>> actual = extractor.getStat();
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test total request counter.")
    void testTotalRequestCounter() {
        //Given
        long expected = 3L;
        //When
        StatExtractor<Long> extractor = new TotalRequestCountStatExtractor();
        getPreparedStream().forEach(extractor);
        Long actual = extractor.getStat();
        //Then
        assertThat(actual).isEqualTo(expected);
    }

}
