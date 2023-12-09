package edu.hw10.task2;

import edu.hw10.task2.cacheproxy.CacheProxy;
import edu.hw10.task2.testsamples.BasicNumberDivider;
import edu.hw10.task2.testsamples.Calculator;
import edu.hw10.task2.testsamples.FibonacciCalculator;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import edu.hw10.task2.testsamples.NumberDivider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CacheProxyTest {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String LINE_SEPARATOR = System.lineSeparator();

    @Test
    @DisplayName("Test null args.")
    void testNull() {
        assertThatThrownBy(() -> {
            CacheProxy<Calculator> cacheProxy = CacheProxy.getCacheProxyInstance(null);
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Arguments must be not null.");
    }

    @Test
    @DisplayName("Test fibonacci cache.")
    void testFibonacciCache() throws IOException {
        //Given
        Path filePath = Path.of("src/test/resources/hw10testresources/fibonaccicache.txt");
        Map<List<Object>, Object> expectedRuntimeCache = Map.of(
            List.of(0), 0L,
            List.of(1), 1L,
            List.of(2), 1L,
            List.of(3), 2L,
            List.of(4), 3L,
            List.of(5), 5L,
            List.of(6), 8L,
            List.of(10), 55L
        );
        String expectedCacheFileContent = "[0] : 0" + LINE_SEPARATOR +
                                          "[1] : 1" + LINE_SEPARATOR +
                                          "[2] : 1" + LINE_SEPARATOR +
                                          "[3] : 2" + LINE_SEPARATOR +
                                          "[4] : 3" + LINE_SEPARATOR +
                                          "[5] : 5" + LINE_SEPARATOR +
                                          "[6] : 8" + LINE_SEPARATOR +
                                          "[10] : 55" + LINE_SEPARATOR;
        //When
        CacheProxy<Calculator> cacheProxy = CacheProxy.getCacheProxyInstance(new FibonacciCalculator());
        Calculator calculator = cacheProxy.getTargetProxy();
        for (int i = 0; i < 7; i++) {
            calculator.calc(i);
        }
        calculator.calc(10);
        LOGGER.info(calculator.toString());
        for (int i = 6; i >= 0; i--) {
            calculator.calc(i);
        }
        //Then
        Assertions.assertAll(
            () -> assertThat(cacheProxy.getRuntimeCache()).containsExactlyInAnyOrderEntriesOf(expectedRuntimeCache),
            () -> {
                StringBuilder builder = new StringBuilder();
                try (
                    BufferedReader reader
                        = Files.newBufferedReader(filePath)
                ) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                        builder.append(System.lineSeparator());
                    }
                }
                assertThat(builder.toString()).isEqualTo(expectedCacheFileContent);
            }
        );
        Files.deleteIfExists(filePath);
    }

    @Test
    @DisplayName("Test number divider cache.")
    void testNumberDividerCache() throws IOException {
        //Given
        Path filePath = Path.of("src/test/resources/hw10testresources/dividercache.txt");
        Map<List<Object>, Object> expectedRuntimeCache = Map.of(
            List.of(10L, 2.5f), 4.0,
            List.of(30.0, 10), 3.0,
            List.of(10.1, 2), 5.05
        );
        String expectedCacheFileContent = "[10, 2.5] : 4.0" + LINE_SEPARATOR +
                                          "[30.0, 10] : 3.0" + LINE_SEPARATOR +
                                          "[10.1, 2] : 5.05" + LINE_SEPARATOR;
        //When
        CacheProxy<NumberDivider> cacheProxy = CacheProxy.getCacheProxyInstance(new BasicNumberDivider());
        NumberDivider divider = cacheProxy.getTargetProxy();
        divider.divide(10L, 2.5f);
        LOGGER.info(divider.toString());
        divider.divide(30.0, 10);
        divider.divide(10.1, 2);
        //Then
        Assertions.assertAll(
            () -> assertThat(cacheProxy.getRuntimeCache()).containsExactlyInAnyOrderEntriesOf(expectedRuntimeCache),
            () -> {
                StringBuilder builder = new StringBuilder();
                try (
                    BufferedReader reader
                        = Files.newBufferedReader(filePath)
                ) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                        builder.append(System.lineSeparator());
                    }
                }
                assertThat(builder.toString()).isEqualTo(expectedCacheFileContent);
            }
        );
        Files.deleteIfExists(filePath);
    }

}
