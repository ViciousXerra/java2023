package edu.hw6.task3.filters;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.PathMatcher;
import java.util.regex.Pattern;

public final class FiltersUtil {

    private final static String NULL_ARG_MESSAGE = "Arguments can't be null.";

    //Some attribute standalone filters.
    public static AbstractFilter isExisting = Files::exists;
    public static AbstractFilter isRegularFile = Files::isRegularFile;
    public static AbstractFilter isReadable = Files::isReadable;
    public static AbstractFilter isWritable = Files::isWritable;

    private FiltersUtil() {

    }

    public static AbstractFilter getAllAttributeFilters() {
        return isExisting
            .and(isRegularFile)
            .and(isReadable)
            .and(isWritable);
    }

    public static AbstractFilter getFilterBySize(long sizeInBytes, boolean larger) {
        if (larger) {
            return path -> Files.size(path) < sizeInBytes;
        } else {
            return path -> Files.size(path) >= sizeInBytes;
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
        return path -> {
            String fileName = path.getFileName().toString();
            Pattern pattern = Pattern.compile(regex);
            return pattern.matcher(fileName).find();
        };
    }

}


















