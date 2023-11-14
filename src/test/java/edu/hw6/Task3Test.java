package edu.hw6;

import edu.hw6.task3.AbstractFilter;
import edu.hw6.task3.FilterUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task3Test {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String NULL_ARG_MESSAGE = "Arguments can't be null.";
    private final static String FILES_FOLDER = "src/test/resources/hw6testresources/task3resources";

    @Test
    @DisplayName("Test passing null.")
    void testPassNull() {
        assertThatThrownBy(() -> FilterUtils.getFileExtensionFilter(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_ARG_MESSAGE);
        assertThatThrownBy(() -> FilterUtils.getGlobMatchesFilter(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_ARG_MESSAGE);
        assertThatThrownBy(() -> FilterUtils.getFileNameRegexFilter(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_ARG_MESSAGE);
    }

    @Test
    @DisplayName("Test passing empty array of bytes.")
    void testEmptyByteArray() {
        assertThatThrownBy(FilterUtils::getFileExtensionFilter)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Hex values can't be zero length.");
    }

    @Test
    @DisplayName("Test passing string without starts with \"glob\"")
    void nonGlobStr() {
        assertThatThrownBy(() -> FilterUtils.getGlobMatchesFilter("fgsdgbsdfhas"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Method allows only glob patterns.");
    }

    @Test
    @DisplayName("Test passing string without starts with \"regex\"")
    void nonRegexStr() {
        assertThatThrownBy(() -> FilterUtils.getFileNameRegexFilter("fgsdgbsdfhas"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Method allows only regex patterns.");
    }

    @Test
    @DisplayName("Test attributes filter chain for files.")
    void testAttributeFiltersForFiles() {
        //Given
        List<String> expectedFileNames = List.of(
            "jaguar.png",
            "Martensite-2022.pdf",
            "some_random_text.rtf"
        );
        //When
        AbstractFilter filter = FilterUtils.isExisting
            .and(FilterUtils.isRegularFile)
            .and(FilterUtils.isReadable);
        List<String> actualFileNames = new ArrayList<>();
        try {
            Files.newDirectoryStream(Path.of(FILES_FOLDER), filter)
                .forEach(path -> actualFileNames.add(path.getFileName().toString()));
        } catch (IOException e) {
            LOGGER.error("Caught I/O exception. Unable to get directory stream");
        }
        //Then
        assertThat(actualFileNames).containsExactlyInAnyOrderElementsOf(expectedFileNames);
    }

    @Test
    @DisplayName("Test filters for pdf magical header numbers.")
    void testPDFMagicalHeaderNumbersSignatureFilter() {
        //Given
        List<String> expectedFileNames = List.of(
            "Martensite-2022.pdf"
        );
        //When
        byte[] pdfHeaderSignatureBytes = new byte[] {0x25, 0x50, 0x44, 0x46, 0x2D};
        AbstractFilter filter = FilterUtils.isExisting
            .and(FilterUtils.isRegularFile)
            .and(FilterUtils.getFileExtensionFilter(pdfHeaderSignatureBytes));
        List<String> actualFileNames = new ArrayList<>();
        try {
            Files.newDirectoryStream(Path.of(FILES_FOLDER), filter)
                .forEach(path -> actualFileNames.add(path.getFileName().toString()));
        } catch (IOException e) {
            LOGGER.error("Caught I/O exception. Unable to get directory stream");
        }
        //Then
        assertThat(actualFileNames).containsExactlyInAnyOrderElementsOf(expectedFileNames);
    }

    @Test
    @DisplayName("Test glob pattern filter.")
    void testGlobPatternFilter() {
        //Given
        List<String> expectedFileNames = List.of(
            "jaguar.png"
        );
        //When
        AbstractFilter filter = FilterUtils.isExisting
            .and(FilterUtils.isRegularFile)
            .and(FilterUtils.getGlobMatchesFilter("glob:**.png"));
        List<String> actualFileNames = new ArrayList<>();
        try {
            Files.newDirectoryStream(Path.of(FILES_FOLDER), filter)
                .forEach(path -> actualFileNames.add(path.getFileName().toString()));
        } catch (IOException e) {
            LOGGER.error("Caught I/O exception. Unable to get directory stream");
        }
        //Then
        assertThat(actualFileNames).containsExactlyInAnyOrderElementsOf(expectedFileNames);
    }

    @Test
    @DisplayName("Test regex pattern filter.")
    void testRegexPatternFilter() {
        //Given
        List<String> expectedFileNames = List.of(
            "Martensite-2022.pdf"
        );
        //When
        AbstractFilter filter = FilterUtils.isExisting
            .and(FilterUtils.getFileNameRegexFilter("regex:(.+\\d{4}.+)"));
        List<String> actualFileNames = new ArrayList<>();
        try {
            Files.newDirectoryStream(Path.of(FILES_FOLDER), filter)
                .forEach(path -> actualFileNames.add(path.getFileName().toString()));
        } catch (IOException e) {
            LOGGER.error("Caught I/O exception. Unable to get directory stream");
        }
        //Then
        assertThat(actualFileNames).containsExactlyInAnyOrderElementsOf(expectedFileNames);
    }

    @Test
    @DisplayName("Test \"larger than\" size filter.")
    void testSizeFilter1() {
        //Given
        List<String> expectedFileNames = List.of(
            "Martensite-2022.pdf"
        );
        //When
        AbstractFilter filter = FilterUtils.isExisting
            .and(FilterUtils.isRegularFile)
            .and(FilterUtils.getFilterBySize(1_000_000L, true));
        List<String> actualFileNames = new ArrayList<>();
        try {
            Files.newDirectoryStream(Path.of(FILES_FOLDER), filter)
                .forEach(path -> actualFileNames.add(path.getFileName().toString()));
        } catch (IOException e) {
            LOGGER.error("Caught I/O exception. Unable to get directory stream");
        }
        //Then
        assertThat(actualFileNames).containsExactlyInAnyOrderElementsOf(expectedFileNames);
    }

    @Test
    @DisplayName("Test \"less than\" size filter.")
    void testSizeFilter2() {
        //Given
        List<String> expectedFileNames = List.of(
            "some_random_text.rtf",
            "jaguar.png"
        );
        //When
        AbstractFilter filter = FilterUtils.isExisting
            .and(FilterUtils.isRegularFile)
            .and(FilterUtils.getFilterBySize(1_000_000L, false));
        List<String> actualFileNames = new ArrayList<>();
        try {
            Files.newDirectoryStream(Path.of(FILES_FOLDER), filter)
                .forEach(path -> actualFileNames.add(path.getFileName().toString()));
        } catch (IOException e) {
            LOGGER.error("Caught I/O exception. Unable to get directory stream");
        }
        //Then
        assertThat(actualFileNames).containsExactlyInAnyOrderElementsOf(expectedFileNames);
    }

}
