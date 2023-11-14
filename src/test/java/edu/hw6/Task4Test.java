package edu.hw6;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task4Test {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String FILES_FOLDER = "src/test/resources/hw6testresources/task4resources";

    @Test
    @DisplayName("Passing null path arg.")
    void testNullPath() {
        assertThatThrownBy(() -> Task4.task4Demo(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Path can't be null.");
    }

    @Test
    @DisplayName("Passing directory path arg.")
    void testDirectoryPath() {
        assertThatThrownBy(() -> Task4.task4Demo(Path.of(FILES_FOLDER)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Path can't be a directory.");
    }

    @Test
    @DisplayName("Test Task4 demo method.")
    void testTask4Demo() {
        //Given
        String expected = "Programming is learned by writing programs. - Brian Kernighan";
        //When
        Path file = Path.of(FILES_FOLDER, "quotation.txt");
        Task4.task4Demo(file);
        StringBuilder actual = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            while (reader.ready()) {
                actual.append(reader.readLine());
            }
        } catch (IOException e) {
            LOGGER.error("Caught I/O exception.");
        }
        //Then
        assertThat(actual.toString()).isEqualTo(expected);
    }

}
