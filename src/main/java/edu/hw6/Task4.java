package edu.hw6;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Task4 {

    private final static Logger LOGGER = LogManager.getLogger();

    private Task4() {

    }

    public static void task4Demo(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Path can't be null.");
        }
        if (Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path can't be a directory.");
        }
        try (
            OutputStream fileOut = Files.newOutputStream(path);
            CheckedOutputStream checkedOut = new CheckedOutputStream(fileOut, new Adler32());
            BufferedOutputStream bufferedOut = new BufferedOutputStream(checkedOut);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(bufferedOut, StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(outputStreamWriter)
        ) {
            writer.println("Programming is learned by writing programs. - Brian Kernighan");
        } catch (IOException ignored) {
            LOGGER.error("Unable to write to file.");
        }
    }

}
