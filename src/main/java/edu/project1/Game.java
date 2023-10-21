package edu.project1;

import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

final class Game {

    private String guessedWord;
    private char[] userTable;
    private int totalCorrectGuessedChars;
    private int attempts;
    private final int attemptsLimit;
    private final WordProvider provider;

    Game(@NotNull WordProvider provider, int attemptsLimit) throws EmptyWordsStockException {
        this.provider = provider;
        this.attemptsLimit = attemptsLimit;
    }

    GameCycleInfo processAttempt(char input) {
        int correctGuessedChars = 0;
        for (int i = 0; i < guessedWord.length(); i++) {
            if (guessedWord.charAt(i) == input && userTable[i] != input) {
                userTable[i] = guessedWord.charAt(i);
                totalCorrectGuessedChars++;
                correctGuessedChars++;
            }
        }
        if (correctGuessedChars > 0) {
            return new GameCycleInfo(userTable, attempts, attemptsLimit, correctGuessedChars, isPlayerWin());
        } else {
            return new GameCycleInfo(userTable, ++attempts, attemptsLimit, correctGuessedChars, false);
        }
    }

    void reset() throws EmptyWordsStockException {
        guessedWord = provider.getRandomWord();
        userTable = new char[guessedWord.length()];
        Arrays.fill(userTable, '*');
        attempts = 0;
        totalCorrectGuessedChars = 0;
    }

    String getAnswer() {
        return guessedWord;
    }

    private boolean isPlayerWin() {
        return totalCorrectGuessedChars == guessedWord.length();
    }

}
