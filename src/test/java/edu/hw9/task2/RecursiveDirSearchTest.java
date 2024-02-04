package edu.hw9.task2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RecursiveDirSearchTest {

    private final static String TARGET_DIRECTORY = "src/test/resources/hw9testresources";


    @ParameterizedTest
    @DisplayName("Test non-positive file count parameter.")
    @ValueSource(longs = {Long.MIN_VALUE, -1})
    void testFileCountParameter(long fileCount) {
        assertThatThrownBy(() -> Task2.getDirectoriesWithFileCountLargerThan(Path.of(""), fileCount))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Files count must be a positive num.");
    }

    @Test
    @DisplayName("Test recursive directory search.")
    void testRecursiveSearch() {
        //Given
        Path expectedPath = Path.of(TARGET_DIRECTORY, "temporaryDir");
        //When
        try {
            Path targetDir = Path.of(TARGET_DIRECTORY);
            Path temporaryDir = Files.createDirectory(expectedPath);
            for (int i = 0; i < 1500; i++) {
                String fileNameTemplate = "file%d.txt";
                Files.createFile(Path.of(temporaryDir.toString(), String.format(fileNameTemplate, i)));
            }
            List<Path> actualList = Task2.getDirectoriesWithFileCountLargerThan(targetDir, 1000);
            //Then
            assertThat(actualList).containsExactly(expectedPath);
            //CleanUp
            File dir = temporaryDir.toFile();
            for (File file : dir.listFiles()) {
                file.delete();
            }
            dir.delete();
        } catch (IOException e) {
            throw new RuntimeException("I/O error occurs", e);
        }
    }

}
