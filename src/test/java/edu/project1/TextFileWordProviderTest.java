package edu.project1;

import java.io.FileNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TextFileWordProviderTest {

    @Test
    @DisplayName("Test invalid filepath")
    void testInvalidFilePath() {
        assertThatThrownBy(() -> {
            TextFileWordProvider provider = new TextFileWordProvider(true, "/");
        })
            .isInstanceOf(FileNotFoundException.class);
    }

    @Test
    @DisplayName("Test valid file with unacceptable words")
    void tesUnacceptableWordDictionary() {
        assertThatThrownBy(() -> {
            TextFileWordProvider provider = new TextFileWordProvider(true,
                "src/test/resources/project1testresources/UnacceptableWordDictionary.txt");
            provider.getRandomWord();
        })
            .isInstanceOf(EmptyWordsStockException.class)
            .hasMessage("\nWords stock is empty.")
            .hasCauseInstanceOf(RuntimeException.class);
    }

}
