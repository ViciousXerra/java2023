package edu.hw6.task3;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.PathMatcher;

public final class FilterUtils {

    private final static String NULL_ARG_MESSAGE = "Arguments can't be null.";

    //Some attribute standalone filters.
    public static AbstractFilter isExisting = Files::exists;
    public static AbstractFilter isRegularFile = Files::isRegularFile;
    public static AbstractFilter isReadable = Files::isReadable;
    public static AbstractFilter isDirectory = Files::isDirectory;

    private FilterUtils() {

    }

    public static AbstractFilter getFilterBySize(long sizeInBytes, boolean larger) {
        if (larger) {
            return path -> Files.size(path) >= sizeInBytes;
        } else {
            return path -> Files.size(path) < sizeInBytes;
        }
    }

    public static AbstractFilter getFileExtensionFilter(byte... headerHexValues) {
        if (headerHexValues == null) {
            throw new IllegalArgumentException(NULL_ARG_MESSAGE);
        }
        if (headerHexValues.length == 0) {
            throw new IllegalArgumentException("Hex values can't be zero length.");
        }
        return path -> {
            boolean match = true;
            ByteBuffer bb = ByteBuffer.allocate(headerHexValues.length);
            try (FileChannel channel = new FileInputStream(path.toFile()).getChannel()) {
                channel.read(bb);
            }
            bb.flip();
            for (byte hexValue : headerHexValues) {
                if (hexValue != bb.get()) {
                    match = false;
                    break;
                }
            }
            return match;
        };
    }

    public static AbstractFilter getGlobMatchesFilter(String glob) {
        if (glob == null) {
            throw new IllegalArgumentException(NULL_ARG_MESSAGE);
        }
        if (!glob.startsWith("glob:")) {
            throw new IllegalArgumentException("Method allows only glob patterns.");
        }
        return path -> {
            PathMatcher matcher = FileSystems.getDefault().getPathMatcher(glob);
            return matcher.matches(path);
        };
    }

    public static AbstractFilter getFileNameRegexFilter(String regex) {
        if (regex == null) {
            throw new IllegalArgumentException(NULL_ARG_MESSAGE);
        }
        if (!regex.startsWith("regex:")) {
            throw new IllegalArgumentException("Method allows only regex patterns.");
        }
        return path -> {
            PathMatcher matcher = FileSystems.getDefault().getPathMatcher(regex);
            return matcher.matches(path);
        };
    }

}


















