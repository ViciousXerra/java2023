package edu.project1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import static org.assertj.core.api.Assertions.assertThat;

public class GameTest {

    @Test
    @DisplayName("Test word")
    void testWord() throws FileNotFoundException, EmptyWordsStockException {
        //Given
        TextFileWordProvider provider = new TextFileWordProvider(false,
            "src/test/resources/project1testresources/WordDictionary.txt");
        int attemptsLimit = 1;
        Game game = new Game(provider, attemptsLimit);
        game.reset();
        String expected = "apricot";
        //When
        String actual = game.getAnswer();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test attempts limit violation")
    void testAttemptsViolation() throws FileNotFoundException, EmptyWordsStockException {
        //Given
        TextFileWordProvider provider = new TextFileWordProvider(false,
            "src/test/resources/project1testresources/WordDictionary.txt");
        int attemptsLimit = 1;
        Game game = new Game(provider, attemptsLimit);
        game.reset();
        //When
        GameCycleInfo gameInfo = game.processAttempt('j');
        //Then
        assertThat(gameInfo.isEnded()).isTrue();
    }

    @Test
    @DisplayName("Test correct guesses")
    void testCorrectGuesses() throws FileNotFoundException, EmptyWordsStockException {
        //Given
        TextFileWordProvider provider = new TextFileWordProvider(false,
            "src/test/resources/project1testresources/WordDictionary.txt");
        int attemptsLimit = 1;
        Game game = new Game(provider, attemptsLimit);
        game.reset();
        String expected = "apricot";
        //When
        GameCycleInfo gameCycleInfo = null;
        for (char x: expected.toCharArray()) {
            gameCycleInfo = game.processAttempt(x);
        }
        boolean actual = gameCycleInfo.isPlayerWin();
        assertThat(actual).isTrue();
    }
}
