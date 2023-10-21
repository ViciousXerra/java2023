package edu.project1;

import java.io.FileNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HangmanTest {
    @Test
    @DisplayName("Invalid source filepath test")
    @Order(1)
    void testInvalidFileSource() {
        assertThatThrownBy(() -> {
            Hangman hangman = new Hangman("", true, "", 5);
            hangman.run();
        })
            .isInstanceOf(FileNotFoundException.class);
    }

    @Test
    @DisplayName("Valid source filepath with unacceptable words")
    @Order(2)
    void testUnacceptableWords() {
        //Given
        String filePath = "src/test/resources/project1testresources/UnacceptableWordDictionary.txt";
        //Then
        assertThatCode(() -> {
            Hangman hangman = new Hangman("", true, filePath, 5);
            hangman.run();
        })
            .doesNotThrowAnyException();
    }
    /*
    В следующих тестах SUREFIRE ругается:
    # Created at 2023-10-21T18:42:13.934
    [SUREFIRE] std/in stream corrupted
    Скорее всего, на следующий участок кода, поскольку изначально в сканер передавался поток ввода.

    InputStream inputStream = new ByteArrayInputStream(consoleInput.getBytes(StandardCharsets.UTF_8));

    Также, в Hangman в сканер передавался стандартный поток ввода System.in, как я его убрал из кода,
    всё исчезло. Интернет не особо пестрит решениями этой проблемы, кроме как отвязки через
    ProcessBuilder.
    */

    @Test
    @DisplayName("Test immediate exit")
    @Order(3)
    void testImmediateExit() {
        //Given
        String filePath = "src/test/resources/project1testresources/WordDictionary.txt";
        String consoleInput = """
            a
            p
            r
            exit""";
        //Then
        assertThatCode(() -> {
            Hangman hangman = new Hangman(consoleInput, false, filePath, 5);
            hangman.run();
        })
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Test max attempts violation")
    @Order(4)
    void testMaxAttemptsViolation() {
        //Given
        String filePath = "src/test/resources/project1testresources/WordDictionary.txt";
        int attemptsLimit = 5;
        String consoleInput = """
            x
            b
            q
            z
            v
            quit""";
        //Then
        assertThatCode(() -> {
            Hangman hangman = new Hangman(consoleInput, false, filePath, attemptsLimit);
            hangman.run();
        })
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Test correct guesses on 3 words")
    @Order(5)
    void testCorrectGuesses() {
        //Given
        String filePath = "src/test/resources/project1testresources/WordDictionary.txt";
        int attemptsLimit = 5;
        String consoleInput = """
            gzdoniggoa
            a
            p
            r
            i
            c
            o
            t
            new
            b
            n
            a
            new
            a
            p
            l
            e
            new""";
        //Then
        assertThatCode(() -> {
            Hangman hangman = new Hangman(consoleInput, false, filePath, attemptsLimit);
            hangman.run();
        })
            .doesNotThrowAnyException();
    }
}
