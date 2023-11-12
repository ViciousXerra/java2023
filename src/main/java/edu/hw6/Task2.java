package edu.hw6;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        File sourceFile = source.toFile();
        if (sourceFile.isDirectory()) {
            throw new IllegalArgumentException(PATH_DIRECTORY_RESTRICTION);
        }
        if (!sourceFile.exists()) {
            throw new IllegalArgumentException(FILE_NOT_EXIST_MESSAGE);
        }
        long cloneCount;
        try {
            cloneCount = getCloneCount(sourceFile);
        } catch (IOException e) {
            LOGGER.info("Unable to track before cloned files.");
            return;
        }
        String copyName = cloneCount == 0
            ? String.format("%s - копия.%s", getFileName(sourceFile), getFileExtension(sourceFile))
            : String.format("%s - копия (%d).%s", getFileName(sourceFile), ++cloneCount, getFileExtension(sourceFile));
        Path dest = Paths.get((sourceFile.getParent() == null ? "" : sourceFile.getParent()), copyName);
        try (
            FileChannel original = new FileInputStream(sourceFile).getChannel();
            FileChannel copy = new FileOutputStream(dest.toFile()).getChannel()
        ) {
            copy.transferFrom(original, 0, original.size());
        } catch (IOException e) {
            LOGGER.info("Unable to clone file.");
        }
    }

    private static String getFileName(File source) {
        String sourceFileName = source.getName();
        return sourceFileName.substring(0, getDotIndex(sourceFileName));
    }

    private static String getFileExtension(File source) {
        String sourceFileName = source.getName();
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

    private static long getCloneCount(File source) throws IOException {
        Pattern pattern = Pattern.compile("^" + getFileName(source) + " - копия.*\\." + getFileExtension(source) +"$");
        return
            Files
            .list(Paths.get(source.getParent()))
            .filter(path -> {
                Matcher matcher = pattern.matcher(path.toFile().getName());
                return matcher.find();
            })
            .count();
    }

}
