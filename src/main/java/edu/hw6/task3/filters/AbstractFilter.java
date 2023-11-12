package edu.hw6.task3.filters;

import java.nio.file.DirectoryStream;
import java.nio.file.Path;

@FunctionalInterface
public interface AbstractFilter extends DirectoryStream.Filter<Path> {

    default AbstractFilter and(AbstractFilter filter) {
        return path -> this.accept(path) && filter.accept(path);
    }

}
