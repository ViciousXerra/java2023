package edu.project3.logextractorstests;

import edu.project3.logstreamextractors.LocalFileLogStreamExtractor;
import edu.project3.logstreamextractors.LogRecord;
import edu.project3.logstreamextractors.LogStreamExtractor;
import edu.project3.logstreamextractors.LogUtils;
import java.time.OffsetDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LocalFileLogExtractorTest {

    private static Stream<LogRecord> getExpectedStream() {
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
            ),
            new LogRecord("173.203.139.108", "-",
                OffsetDateTime.parse("17/May/2015:08:05:08 +0000", LogUtils.OFFSET_DATE_TIME_FORMATTER),
                LogRecord.RequestType.GET, "/downloads/product_1",
                "304", 0L, "-", "Debian APT-HTTP/1.3 (0.9.7.9)"
            )
        );
    }

    @Test
    @DisplayName("Unexisting file test.")
    void testUnexistingFile() {
        assertThatThrownBy(() -> new LocalFileLogStreamExtractor("src/test/resources/project3resources/testinputs"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Unable to resolve path. Please, specify your path arguments.");
    }

    @Test
    @DisplayName("Test local file log extraction.")
    void testLocalFileExtraction() {
        //Given
        Stream<LogRecord> expected = getExpectedStream();
        //When
        LogStreamExtractor extractor = new LocalFileLogStreamExtractor(
            "src/test/resources/project3resources/testinginputs/*.txt");
        Stream<LogRecord> actual = extractor.extract();
        //Then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected.toList());
    }

}
