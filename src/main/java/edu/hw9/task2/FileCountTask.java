package edu.hw9.task2;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Stream;

final class FileCountTask extends RecursiveTask<List<Path>> {

    private final Path directory;
    private final long borderCount;

    FileCountTask(Path directory, long count) {
        this.directory = directory;
        this.borderCount = count;
    }

    @Override
    protected List<Path> compute() {
        long fileCount = 0;
        FileCountTask subTask;
        List<FileCountTask> subTasks = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path path : stream) {
                if (Files.isRegularFile(path)) {
                    fileCount++;
                    continue;
                }
                if (Files.isDirectory(path)) {
                    subTask = new FileCountTask(path, borderCount);
                    subTask.fork();
                    subTasks.add(subTask);
                }
            }
            try (Stream<Path> result = subTasks.stream().map(RecursiveTask::join).flatMap(Collection::stream)) {
                if (fileCount > borderCount) {
                    return Stream.concat(result, Stream.of(directory)).toList();
                } else {
                    return result.toList();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("I/O error occurs.", e);
        }
    }

}
