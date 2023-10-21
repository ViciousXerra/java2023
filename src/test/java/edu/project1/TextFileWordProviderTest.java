package edu.project1;

import java.io.FileNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TextFileWordProviderTest {

    @Test
    @DisplayName("Test invalid file path")
    void testInvalidFilePath() {
        assertThatThrownBy(() -> {
            TextFileWordProvider provider = new TextFileWordProvider(true, "/");
        })
            .isInstanceOf(FileNotFoundException.class);
    }

    @Test
    @DisplayName("Test valid file path with unacceptable words with random")
    void tesUnacceptableWordDictionaryWithRandom() {
        assertThatThrownBy(() -> {
            TextFileWordProvider provider = new TextFileWordProvider(true,
                "src/test/resources/project1testresources/UnacceptableWordDictionary.txt");
            provider.getRandomWord();
        })
            .isInstanceOf(EmptyWordsStockException.class)
            .hasMessage("\nWords stock is empty.")
            .hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Test words buffer exhaust")
    void testWordsBufferExhaust() {
        assertThatThrownBy(() -> {
            TextFileWordProvider provider = new TextFileWordProvider(false,
                "src/test/resources/project1testresources/UnacceptableWordDictionary.txt");
            provider.getRandomWord();
            provider.getRandomWord();
            provider.getRandomWord();
            provider.getRandomWord();
        })
            .isInstanceOf(EmptyWordsStockException.class)
            .hasMessage("\nWords stock is empty.")
            .hasCauseInstanceOf(RuntimeException.class);
    }

}
