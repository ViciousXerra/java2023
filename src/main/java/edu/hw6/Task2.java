package edu.hw6;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Task2 {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String NULL_PATH_MESSAGE = "Path can't be null.";
    private final static String PATH_DIRECTORY_RESTRICTION = "Path can't be a directory.";
    private final static String FILE_NOT_EXIST_MESSAGE = "File does not exist.";

    private Task2() {

    }

    public static void cloneFile(Path source) {
        if (source == null) {
            throw new IllegalArgumentException(NULL_PATH_MESSAGE);
        }
        if (Files.isDirectory(source)) {
            throw new IllegalArgumentException(PATH_DIRECTORY_RESTRICTION);
        }
        if (!Files.exists(source)) {
            throw new IllegalArgumentException(FILE_NOT_EXIST_MESSAGE);
        }
        long cloneCount;
        try {
            cloneCount = getCloneCount(source);
        } catch (IOException e) {
            LOGGER.error("Caught I/O Exception. Unable to track before cloned files.");
            return;
        }
        String copyName = cloneCount == 0
            ? String.format("%s - копия.%s", getFileName(source), getFileExtension(source))
            : String.format("%s - копия (%d).%s", getFileName(source), ++cloneCount, getFileExtension(source));
        Path dest = Paths.get((source.getParent() == null ? "" : source.getParent().toString()), copyName);
        try (
            FileChannel original = new FileInputStream(source.toFile()).getChannel();
            FileChannel copy = new FileOutputStream(dest.toFile()).getChannel()
        ) {
            copy.transferFrom(original, 0, original.size());
        } catch (IOException e) {
            LOGGER.error("Caught I/O Exception. Unable to clone file.");
        }
    }

    private static String getFileName(Path source) {
        String sourceFileName = source.getFileName().toString();
        return sourceFileName.substring(0, getDotIndex(sourceFileName));
    }

    private static String getFileExtension(Path source) {
        String sourceFileName = source.getFileName().toString();
        return sourceFileName.substring(getDotIndex(sourceFileName) + 1);
    }

    private static int getDotIndex(String fileName) {
        int dotIndex;
        for (dotIndex = 0; dotIndex < fileName.length(); dotIndex++) {
            if (fileName.charAt(dotIndex) == '.') {
                break;
            }
        }
        return dotIndex;
    }

    private static long getCloneCount(Path source) throws IOException {
        Pattern pattern =
            Pattern.compile("^" + getFileName(source) + " - копия.*\\." + getFileExtension(source) + "$");
        return
            Files
            .list(source.getParent())
            .filter(path -> {
                Matcher matcher = pattern.matcher(path.toFile().getName());
                return matcher.find();
            })
            .count();
    }

}
