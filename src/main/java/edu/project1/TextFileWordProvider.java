package edu.project1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

final class TextFileWordProvider implements WordProvider {

    private final static File FILE = new File("src/main/resources/project1resources/WordDictionary.txt");
    private final static String DELIMETER_PATTERN = "[\r\n]+";
    private final List<String> words;
    private final boolean isRandomProvided;

    /**
     * Constructs new word provider linked with text file source.
     *
     * @param isRandomProvided if a random choice is expected, it has to be true. Otherwise (from last to first),
     *                         it has to be false.
     * @param pathName A pathname string
     * @throws FileNotFoundException if the file does not exist.
     */
    TextFileWordProvider(boolean isRandomProvided, String pathName) throws FileNotFoundException {
        this.isRandomProvided = isRandomProvided;
        Scanner scanner = pathName == null ? new Scanner(FILE) : new Scanner(new File(pathName));
        try (scanner) {
            scanner.useDelimiter(DELIMETER_PATTERN);
            words = scanner
                .tokens()
                .filter(w -> w.length() > 1 && !w.contains("-") && !w.contains(" "))
                .collect(Collectors.toList());
        }
    }

    /**
     * Constructs new word provider linked with text file source.
     *
     * @param isRandomProvided if a random choice is expected, it has to be true. Otherwise (from last to first),
     *                         it has to be false.
     * @throws FileNotFoundException if the file does not exist.
     */
    TextFileWordProvider(boolean isRandomProvided) throws FileNotFoundException {
        this(isRandomProvided, null);
    }

    @Override
    @NotNull public String getRandomWord() throws EmptyWordsStockException {
        String word;
        if (words.size() == 0) {
            throw new EmptyWordsStockException("\nWords stock is empty.");
        }
        word = isRandomProvided ? words.get(new Random().nextInt(words.size())) : words.get(words.size() - 1);
        words.remove(word);
        return word;
    }

}
