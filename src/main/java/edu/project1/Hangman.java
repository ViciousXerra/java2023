package edu.project1;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Hangman {

    private final static Logger LOGGER = LogManager.getLogger();
    private final InputStream source;
    private final boolean isRandomProvided;
    private final String pathName;
    private final int attemptsLimit;

    /**
     * Constructs new Hangman instance.
     * @param source An input stream to be scanned
     * @param isRandomProvided If a random choice is expected, it has to be true. Otherwise, it has to be false
     * @param pathName A source filepath
     * @param attemptsLimit Maximum attempts allowed
     */
    public Hangman(InputStream source, boolean isRandomProvided, String pathName, int attemptsLimit) {
        this.source = source;
        this.isRandomProvided = isRandomProvided;
        this.pathName = pathName;
        this.attemptsLimit = attemptsLimit;
    }

    public void run() throws FileNotFoundException {
        Scanner scanner = new Scanner(source);
        Game game;
        try {
            game = new Game(new TextFileWordProvider(isRandomProvided, pathName), attemptsLimit);
            game.reset();
        } catch (FileNotFoundException e) {
            scanner.close();
            throw e;
        } catch (EmptyWordsStockException e) {
            LOGGER.info(e.getMessage());
            scanner.close();
            return;
        }
        start(scanner, game);
        scanner.close();
    }

    private void start(Scanner scanner, Game game) {
        GameCycleInfo gameInfo;
        String input;
        while (true) {
            LOGGER.info("\nType \"exit\" if you want to end current game immediately.");
            LOGGER.info("\nGuess char:");
            input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            } else if (input.length() > 1) {
                LOGGER.info("\nInvalid input (only 1 character and \"exit\" is valid)");
                continue;
            }
            gameInfo = game.processAttempt(input.charAt(0));
            LOGGER.info(gameInfo.toString());
            if (gameInfo.isPlayerWin() || gameInfo.isEnded()) {
                LOGGER.info(String.format("\nGuessed word: %s\n", game.getAnswer()));
                if (isPlayerChooseQuit(scanner, game)) {
                    break;
                }
            }
        }
    }

    private boolean isPlayerChooseQuit(Scanner scanner, Game game) {
        String input;
        while (true) {
            LOGGER.info("\nType \"new\" to start new game or \"quit\" to quit game session");
            input = scanner.nextLine();
            if (input.equals("new")) {
                try {
                    game.reset();
                } catch (EmptyWordsStockException e) {
                    LOGGER.info(e.getMessage());
                    return true;
                }
                break;
            } else if (input.equals("quit")) {
                return true;
            }
        }
        return false;
    }

}
