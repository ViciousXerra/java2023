package edu.project1;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Hangman {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String DELIMETER_PATTERN = "[\r\n]+";
    private final static String IMMEDIATELY_EXIT_KEYWORD = "exit";
    private final static String NEW_GAME_KEYWORD = "new";
    private final static String QUIT_GAME_KEYWORD = "quit";
    private final InputStream source;
    private final boolean isRandomProvided;
    private final String pathName;
    private final int attemptsLimit;

    /**
     * Constructs new Hangman instance.
     *
     * @param source           An input stream source represents console inputs
     * @param isRandomProvided If a random choice is expected, it has to be true. Otherwise, it has to be false
     * @param pathName         A source filepath
     * @param attemptsLimit    Maximum attempts allowed
     */
    public Hangman(InputStream source, boolean isRandomProvided, String pathName, int attemptsLimit) {
        this.source = source;
        this.isRandomProvided = isRandomProvided;
        this.pathName = pathName;
        this.attemptsLimit = attemptsLimit;
    }

    public void run() throws FileNotFoundException {
        Game game;
        try (Scanner scanner = new Scanner(source)) {
            scanner.useDelimiter(DELIMETER_PATTERN);
            game = new Game(new TextFileWordProvider(isRandomProvided, pathName), attemptsLimit);
            do {
                LOGGER.info("\nNEW SESSION\n");
            } while (isContinue(scanner, game));
        }
    }

    private boolean isContinue(Scanner scanner, Game game) {
        try {
            game.reset();
        } catch (EmptyWordsStockException e) {
            LOGGER.info(e.getMessage());
            return false;
        }
        String input;
        GameCycleInfo gameInfo = new GameCycleInfo();
        do {
            LOGGER.info("\nType \"exit\" if you want to end current game immediately.");
            LOGGER.info("\nGuess char:");
            input = scanner.nextLine();
            if (IMMEDIATELY_EXIT_KEYWORD.equals(input)) {
                return false;
            } else if (input.length() > 1) {
                LOGGER.info("\nInvalid input (only 1 character and \"exit\" is valid)");
                continue;
            }
            gameInfo = game.processAttempt(input.charAt(0));
            LOGGER.info(gameInfo.toString());
        } while (!gameInfo.isEnded());
        LOGGER.info(String.format("\nGuessed word: %s\n", game.getAnswer()));

        return isPlayerChooseContinue(scanner);
    }

    private boolean isPlayerChooseContinue(Scanner scanner) {
        String input;
        boolean isChooseContinue = false;
        boolean chooseNotAcceptedFlag = true;
        do {
            LOGGER.info("\nType \"new\" to start new game or \"quit\" to quit game session");
            input = scanner.nextLine();
            if (QUIT_GAME_KEYWORD.equals(input)) {
                chooseNotAcceptedFlag = false;
            } else if (NEW_GAME_KEYWORD.equals(input)) {
                isChooseContinue = true;
                chooseNotAcceptedFlag = false;
            }
        } while (chooseNotAcceptedFlag);
        return isChooseContinue;
    }

}
