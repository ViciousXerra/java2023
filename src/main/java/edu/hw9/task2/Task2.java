package edu.hw9.task2;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public final class Task2 {

    private Task2() {

    }

    public static List<Path> getDirectoriesWithFileCountLargerThan(Path targetDir, long count) {
        pathValidation(targetDir);
        if (count < 0) {
            throw new IllegalArgumentException("Files count must be a positive num.");
        }
        try (ForkJoinPool service = ForkJoinPool.commonPool()) {
            ForkJoinTask<List<Path>> task = service.submit(new FileCountTask(targetDir, count));
            return task.join();
        }
    }

    public static List<Path> getFilesByPredicate(Path targetDir, DirectoryStream.Filter<Path> filter) {
        pathValidation(targetDir);
        if (filter == null) {
            throw new IllegalArgumentException("Directory stream filter can't be null.");
        }
        try (ForkJoinPool service = ForkJoinPool.commonPool()) {
            ForkJoinTask<List<Path>> task = service.submit(new FileFilterTask(targetDir, filter));
            return task.join();
        }
    }

    private static void pathValidation(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Path can't be null.");
        }
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path must be a directory.");
        }
    }

}
