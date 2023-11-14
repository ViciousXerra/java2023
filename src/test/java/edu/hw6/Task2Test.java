package edu.hw6;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task2Test {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String FILES_FOLDER = "src/test/resources/hw6testresources/task2resources";
    private final static String NULL_PATH_MESSAGE = "Path can't be null.";
    private final static String PATH_DIRECTORY_RESTRICTION = "Path can't be a directory.";
    private final static String FILE_NOT_EXIST_MESSAGE = "File does not exist.";

    @Test
    @DisplayName("Test clone() method.")
    void testClone() {
        //Given
        List<String> expectedFileNames = List.of(
            "sample.txt",
            "sample - копия.txt",
            "sample - копия (2).txt",
            "sample - копия (3).txt",
            "sample - копия (4).txt",
            "sample - копия (5).txt"
        );
        String expectedFileContent = "this is a test";
        //When
        Path path = Path.of(FILES_FOLDER, "sample.txt");
        for (int i = 0; i < 5; i++) {
            Task2.cloneFile(path);
        }
        List<String> actualFileNames = new ArrayList<>();
        List<String> actualFileContent = new ArrayList<>();
        try {
            actualFileNames = Files.list(Path.of(FILES_FOLDER)).map(Path::getFileName).map(Path::toString).toList();
            actualFileContent = Files.list(Path.of(FILES_FOLDER)).map(filePath -> {
                StringBuilder builder = new StringBuilder();
                try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                    while (reader.ready()) {
                        builder.append(reader.readLine());
                    }
                } catch (IOException e) {
                    LOGGER.error("Caught I/O exception.");
                }
                return builder.toString();
            }).toList();
            Files.list(Path.of(FILES_FOLDER)).filter(filePath -> {
                Pattern pattern = Pattern.compile("копия");
                Matcher matcher = pattern.matcher(filePath.getFileName().toString());
                return matcher.find();
            }).forEach(filePath -> {
                try {
                    Files.delete(filePath);
                } catch (IOException e) {
                    LOGGER.error("Caught I/O exception.");
                }
            });
        } catch (IOException e) {
            LOGGER.error("Caught I/O exception.");
        }
        //Then
        assertThat(actualFileNames).containsExactlyInAnyOrderElementsOf(expectedFileNames);
        assertThat(actualFileContent).containsOnly(expectedFileContent);
    }

    @Test
    @DisplayName("Test null path.")
    void testNullPath() {
        assertThatThrownBy(() -> Task2.cloneFile(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_PATH_MESSAGE);
    }

    @Test
    @DisplayName("Test passing directory path.")
    void testDirectoryPath() {
        assertThatThrownBy(() -> Task2.cloneFile(Path.of(FILES_FOLDER)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(PATH_DIRECTORY_RESTRICTION);
    }

    @Test
    @DisplayName("Test unexisting file path.")
    void testUnexistingFilePath() {
        assertThatThrownBy(() -> Task2.cloneFile(Path.of(FILES_FOLDER, "unexistFile.unexist")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(FILE_NOT_EXIST_MESSAGE);
    }

}
