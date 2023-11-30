package edu.hw9.task2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathValidationTest {

    @Test
    @DisplayName("Null path validation.")
    void testNullPath() {
        assertThatThrownBy(() -> Task2.getDirectoriesWithFileCountLargerThan(null, 1000))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Path can't be null.");
    }

    @Test
    @DisplayName("Not a target directory path.")
    void testNotADirectoryPath() {
        assertThatThrownBy(() -> Task2.getDirectoriesWithFileCountLargerThan(
            Path.of("src/test/resources/hw9testresources/jaguar.png"),
            1000)
        )
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Path must be a directory.");
    }

}
