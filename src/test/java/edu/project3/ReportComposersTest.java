package edu.project3;

import edu.project3.logstreamextractors.LogRecord;
import edu.project3.logstreamextractors.LogUtils;
import edu.project3.reportcomposers.LogStatReportComposer;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

class ReportComposersTest {

    private final static String REGEX = "[" + System.lineSeparator() + "\\t]";

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

    private static Object[][] getArgsForComposersTest() {
        return new Object[][] {
            {
                """
                    General Info:
                    Sources:\s
                    logs.txt
                    Date, from: 2011-12-03T00:00Z
                    Date, to: 2011-12-03T00:00Z
                    Total requests: 3
                    Average transferred bytes: 490


                    Additional Info:
                    Frequently queried resources:\s
                    /downloads/product_1 queried 2 times.
                    /downloads/product_2 queried 1 times.

                    Frequently repeated remote addresses:\s
                    217.168.17.5 repeats 2 times.
                    93.180.71.3 repeats 1 times.

                    Frequently repeated response codes:\s
                    200 Success/OK repeats 2 times.
                    304 Not Modified repeats 1 times.

                    Most Frequently repeated request type:\s
                    GET repeats 3 times.

                    """
                    .replaceAll(REGEX, ""),
                "",
                "txt"
            },
            {
                """
                    == General Info

                    [%header,cols=2*]
                    |===
                    |Metrics
                    |Values

                    |Sources
                    |logs.txt +


                    |Date, from
                    |2011-12-03T00:00Z

                    |Date, to
                    |2011-12-03T00:00Z

                    |Total requests
                    |3

                    |Average transferred bytes
                    |490
                    |===

                    === Additional Info

                    * Frequently queried resources:\s
                    +
                    [%header,cols=2*]
                    |===
                    |Resource
                    |Queried, times

                    |/downloads/product_1
                    |2

                    |/downloads/product_2
                    |1
                    |===

                    * Frequently repeated response codes:\s
                    +
                    [%header,cols=3*]
                    |===
                    |Code
                    |Description
                    |Repeated, times

                    |200
                    |Success/OK
                    |2

                    |304
                    |Not Modified
                    |1
                    |===

                    * Frequently repeated remote addresses:\s
                    +
                    [%header,cols=2*]
                    |===
                    |Address
                    |Repeated, times

                    |217.168.17.5
                    |2

                    |93.180.71.3
                    |1
                    |===

                    * Most Frequently repeated request type:\s
                    +
                    GET repeats 3 times.
                    """
                    .replaceAll(REGEX, ""),
                "adoc",
                "adoc"
            },
            {
                """
                    ## General Info
                    |Metrics|Values|
                    |:---|:---|
                    |Source|logs.txt|
                    |Date, from|2011-12-03T00:00Z|
                    |Date, to|2011-12-03T00:00Z|
                    |Total requests|3|
                    |Average transferred bytes|490|

                    ### Additional Info
                    * Frequently queried resources:\s
                        >|Resource|Queried, times|
                        >|:---|:---|
                        >|/downloads/product_1|2|
                        >|/downloads/product_2|1|

                    * Frequently repeated response codes:\s
                        >|Code|Description|Repeated, times|
                        >|:---|:---|:---|
                        >|200|Success/OK|2|
                        >|304|Not Modified|1|

                    * Frequently repeated remote addresses:\s
                        >|Address|Repeated, times|
                        >|:---|:---|
                        >|217.168.17.5|2|
                        >|93.180.71.3|1|

                    * Most Frequently repeated request type:\s
                        >GET repeats 3 times.
                    """
                    .replaceAll(REGEX, ""),
                "markdown",
                "md"
            }
        };
    }

    @ParameterizedTest
    @MethodSource("getArgsForComposersTest")
    @DisplayName("Test report composers.")
    void testComposers(String expected, String format, String fileExtension) {
        //When
        LogStatReportComposer composer = new LogStatReportComposer(
            "2011-12-03",
            "2011-12-03",
            getPreparedStream(),
            Stream.of("logs.txt"),
            format,
            "src/test/resources/project3resources/testingoutputs"
        );
        composer.createReport();
        //Then
        try (BufferedReader reader = Files.newBufferedReader(
            Path.of("src/test/resources/project3resources/testingoutputs/output." + fileExtension))
        ) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String actual = sb.toString();
            assertThat(actual).isEqualTo(expected);
        } catch (IOException e) {

        }
    }

}
