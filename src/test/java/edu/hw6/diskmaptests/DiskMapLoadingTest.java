package edu.hw6.diskmaptests;

import edu.hw6.DiskMap;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiskMapLoadingTest {

    private final static String NULL_RESTRICTION_MESSAGE = "%s can't be null.";
    private final static String PATH_DIRECTORY_VIOLATION_MESSAGE = "Path can't be a directory.";
    private final static String INVALID_FILE_LINE =
        "File line must match %KEY%:%VALUE% pattern, %KEY% and %VALUE% can't be empty.";

    private final static String FILES_FOLDER = "src/test/resources/hw6testresources/diskmapresources";

    @Test
    @DisplayName("Test passing null path.")
    void testNullPath() {
        assertThatThrownBy(() -> {
            DiskMap map = new DiskMap();
            map.loadMap(null);
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(String.format(NULL_RESTRICTION_MESSAGE, "Path"));
    }

    @Test
    @DisplayName("Test map loading from file.")
    void testMapLoading() {
        //Given
        Map<String, String> expected = Map.of(
            "Darth", "Maul",
            "Qui-Gon", "Jinn",
            "Obi-Wan", "Kenobi",
            "Anakin", "Skywalker",
            "Leia", "Organa",
            "Luke", "Skywalker"
        );
        //When
        DiskMap actual = new DiskMap();
        Path path = Path.of(FILES_FOLDER, "fileToRead.txt");
        actual.loadMap(path);
        //Then
        assertThat(actual).containsExactlyInAnyOrderEntriesOf(expected);
    }

    @Test
    @DisplayName("Test map loading from file with invalid line pattern.")
    void testFileWithInvalidLineFormat() {
        //Then
        assertThatThrownBy(() -> {
            DiskMap map = new DiskMap();
            Path path = Path.of(FILES_FOLDER, "invalidFileToRead.txt");
            map.loadMap(path);
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(INVALID_FILE_LINE);
    }

    @Test
    @DisplayName("Test passing path of directory.")
    void testPassingDirectoryPath() {
        //Then
        assertThatThrownBy(() -> {
            DiskMap map = new DiskMap();
            Path path = Path.of(FILES_FOLDER);
            map.loadMap(path);
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(PATH_DIRECTORY_VIOLATION_MESSAGE);
    }
}
