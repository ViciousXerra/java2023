package edu.project1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class GameCycleInfoTest {

    @Test
    @DisplayName("Test \"game over\" info record toString() method")
    void testGameOver() {
        //Given
        String expected = """

            Attempt 4 of 4.
            Word:
            ****
            Game over.
            """;
        //When
        GameCycleInfo gameInfo = new GameCycleInfo(
            new char[] {'*', '*', '*', '*'}, 4, 4, 0, false);
        String actual = gameInfo.toString();
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test \"you win\" info record toString() method")
    void testToStringMethod() {
        //Given
        String expected = """

            Correct!
            5 correct guessed chars.
            Word:
            apple
            Congrats.
            """;
        //When
        GameCycleInfo gameInfo = new GameCycleInfo(
            new char[] {'a', 'p', 'p', 'l', 'e'}, 1, 3, 5, true);
        String actual = gameInfo.toString();
        //Then
        assertThat(actual).isEqualTo(expected);
    }

}

