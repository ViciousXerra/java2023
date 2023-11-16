package edu.hw6.diskmaptests;

import edu.hw6.DiskMap;
import java.io.BufferedReader;
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

class DiskMapSavingTest {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String FILES_FOLDER = "src/test/resources/hw6testresources/diskmapresources";

    @Test
    @DisplayName("Test saving map in file.")
    void testMapSaving() {
        //Given
        List<String> expected = List.of("this is:a test", "nothing:to see");
        //When
        DiskMap map = new DiskMap();
        map.put("this is", "a test");
        map.put("nothing", "to see");
        Path fileToWrite = Path.of(FILES_FOLDER, "fileToWrite.txt");
        map.saveMap(fileToWrite);
        List<String> actual = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(fileToWrite)) {
            while (reader.ready()) {
                actual.add(reader.readLine());
            }
        } catch (IOException e) {
            LOGGER.error("Caught I/O exception on extracting output stream of file bytes.");
        }
        //Then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

}
