package edu.project1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AppTest {

    @Test
    @DisplayName("Invalid source filepath test")
    void testInvalidFileSource() {
        assertThatThrownBy(() -> {
            Hangman hangman = new Hangman(System.in, true, "", 5);
            hangman.run();
        })
            .isInstanceOf(FileNotFoundException.class);
    }

    @Test
    @DisplayName("Valid source filepath with unacceptable words")
    void testUnacceptableWords() {
        //Given
        String filePath = "src/test/resources/project1testresources/UnacceptableWordDictionary.txt";
        //Then
        assertThatCode(() -> {
            Hangman hangman = new Hangman(System.in, true, filePath, 5);
            hangman.run();
        })
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Test immediate exit")
    void testImmediateExit() {
        //Given
        String filePath = "src/test/resources/project1testresources/WordDictionary.txt";
        String consoleInput = """
            a
            p
            r
            exit""";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(consoleInput.getBytes(StandardCharsets.UTF_8));
        //Then
        assertThatCode(() -> {
            Hangman hangman = new Hangman(inputStream, false, filePath, 5);
            hangman.run();
        })
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Test max attempts violation")
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
        ByteArrayInputStream inputStream = new ByteArrayInputStream(consoleInput.getBytes(StandardCharsets.UTF_8));
        //Then
        assertThatCode(() -> {
            Hangman hangman = new Hangman(inputStream, false, filePath, attemptsLimit);
            hangman.run();
        })
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Test correct guesses on 3 words")
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
        ByteArrayInputStream inputStream = new ByteArrayInputStream(consoleInput.getBytes(StandardCharsets.UTF_8));
        //Then
        assertThatCode(() -> {
            Hangman hangman = new Hangman(inputStream, false, filePath, attemptsLimit);
            hangman.run();
        })
            .doesNotThrowAnyException();
    }

}
