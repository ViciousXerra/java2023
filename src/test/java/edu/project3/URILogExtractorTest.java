package edu.project3;

import edu.project3.logstreamextractors.LogRecord;
import edu.project3.logstreamextractors.LogStreamExtractor;
import edu.project3.logstreamextractors.LogUtils;
import java.net.URI;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.stream.Stream;
import edu.project3.logstreamextractors.URILogStreamExtractor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class URILogExtractorTest {

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
            )
        );
    }

    @Test
    @DisplayName("Test with dummy URI")
    void testWithDummyUri() {
        Stream<LogRecord> expected = getExpectedStream();
        URI resource = Path.of("src/test/resources/project3resources/testinginputs/logs.txt").toUri();
        LogStreamExtractor e = new URILogStreamExtractor(resource);
        assertThat(e.extract()).containsExactlyInAnyOrderElementsOf(expected.toList());
    }

}
