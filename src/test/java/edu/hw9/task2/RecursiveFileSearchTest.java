package edu.hw9.task2;

import edu.hw6.task3.FilterUtils;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RecursiveFileSearchTest {

    private final static String TARGET_DIRECTORY = "src/test/resources/hw9testresources";

    @Test
    @DisplayName("Null directory stream filter test.")
    void testNullFilter() {
        assertThatThrownBy(() -> Task2.getFilesByPredicate(Path.of(""), null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Directory stream filter can't be null.");
    }

    @Test
    @DisplayName("Testing recursive file search by given extension predicate.")
    void testRecursiveFileSearchByExtension() {
        //Given
        List<Path> expected1 = List.of(
            Path.of("src/test/resources/hw9testresources/testfolder2/jaguar.png")
        );
        //When
        byte[] pngHeaderHex = new byte[] {(byte) 0x89, 0x50, 0x4e, 0x47};
        DirectoryStream.Filter<Path> filter =
            FilterUtils.getFileExtensionFilter(pngHeaderHex);
        List<Path> actual = Task2.getFilesByPredicate(Path.of(TARGET_DIRECTORY), filter);
        //Then
        assertThat(actual).isEqualTo(expected1);
    }

    @Test
    @DisplayName("Testing recursive file search by given byte size predicate.")
    void testRecursiveFileSearchByByteSize() {
        //Given
        List<Path> expected = List.of(
            Path.of("src/test/resources/hw9testresources/testfolder1/quotation.txt"),
            Path.of("src/test/resources/hw9testresources/testfolder2/jaguar.png"),
            Path.of("src/test/resources/hw9testresources/testfolder3/some_random_text.rtf")
        );
        //When
        DirectoryStream.Filter<Path> filter =
            FilterUtils.getFilterBySize(60L, true);
        List<Path> actual = Task2.getFilesByPredicate(Path.of(TARGET_DIRECTORY), filter);
        //Then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

}
