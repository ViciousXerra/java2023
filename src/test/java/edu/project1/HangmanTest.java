package edu.project1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HangmanTest {

    @Test
    @DisplayName("Invalid source filepath test")
    void testInvalidFileSource() {
        //Given
        String consoleInput = "";
        InputStream source = new ByteArrayInputStream(consoleInput.getBytes(StandardCharsets.UTF_8));
        String pathName = "/";
        int attemptsLimit = 5;
        //Then
        assertThatThrownBy(() -> {
            Hangman hangman = new Hangman(source, true, pathName, attemptsLimit);
            hangman.run();
        })
            .isInstanceOf(FileNotFoundException.class);
    }

    @Test
    @DisplayName("Valid source filepath with unacceptable words")
    void testUnacceptableWords() {
        //Given
        String consoleInput = "";
        InputStream source = new ByteArrayInputStream(consoleInput.getBytes(StandardCharsets.UTF_8));
        String filePath = "src/test/resources/project1testresources/UnacceptableWordDictionary.txt";
        int attemptsLimit = 5;
        //Then
        assertThatCode(() -> {
            Hangman hangman = new Hangman(source, true, filePath, attemptsLimit);
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
        InputStream source = new ByteArrayInputStream(consoleInput.getBytes(StandardCharsets.UTF_8));
        int attemptsLimit = 5;
        //Then
        assertThatCode(() -> {
            Hangman hangman = new Hangman(source, false, filePath, attemptsLimit);
            hangman.run();
        })
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Test max attempts violation")
    void testMaxAttemptsViolation() {
        //Given
        String filePath = "src/test/resources/project1testresources/WordDictionary.txt";
        String consoleInput = """
            x
            b
            q
            z
            v
            quit""";
        InputStream source = new ByteArrayInputStream(consoleInput.getBytes(StandardCharsets.UTF_8));
        int attemptsLimit = 5;
        //Then
        assertThatCode(() -> {
            Hangman hangman = new Hangman(source, false, filePath, attemptsLimit);
            hangman.run();
        })
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Test correct guesses on 3 words")
    void testCorrectGuesses() {
        //Given
        String filePath = "src/test/resources/project1testresources/WordDictionary.txt";
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
        InputStream source = new ByteArrayInputStream(consoleInput.getBytes(StandardCharsets.UTF_8));
        int attemptsLimit = 5;
        //Then
        assertThatCode(() -> {
            Hangman hangman = new Hangman(source, false, filePath, attemptsLimit);
            hangman.run();
        })
            .doesNotThrowAnyException();
    }
}
