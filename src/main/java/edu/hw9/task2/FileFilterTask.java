package edu.hw9.task2;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.RecursiveTask;

class FileFilterTask extends RecursiveTask<List<Path>> {

    private final Path directory;
    private final DirectoryStream.Filter<Path> filter;

    FileFilterTask(Path directory, DirectoryStream.Filter<Path> filter) {
        this.directory = directory;
        this.filter = filter;
    }

    @Override
    protected List<Path> compute() {
        List<Path> matchFiles = new ArrayList<>();
        FileFilterTask subTask;
        List<FileFilterTask> subTasks = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path path : stream) {
                if (Files.isRegularFile(path) && filter.accept(path)) {
                    matchFiles.add(path);
                    continue;
                }
                if (Files.isDirectory(path)) {
                    subTask = new FileFilterTask(path, filter);
                    subTask.fork();
                    subTasks.add(subTask);
                }
            }
            matchFiles.addAll(
                subTasks
                    .stream()
                    .map(RecursiveTask::join)
                    .flatMap(Collection::stream)
                    .toList()
            );
            return matchFiles;
        } catch (IOException e) {
            throw new RuntimeException("I/O error occurs.", e);
        }
    }
}
