package edu.project3.logstreamextractors;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static edu.project3.logstreamextractors.LogUtils.NGINX_LOG_PATTERN;

public class LocalFileLogStreamExtractor extends AbstractLogStreamExtractor {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String CAUGHT_EXCEPTION_MESSAGE_TEMPLATE = "Caught exception: {%s}";

    private final static int KNOWN_PATH_MATCHER_GROUP = 1;
    private final static Pattern FILE_PATH_INPUT_PATTERN =
        Pattern.compile("(([\\w()]+/)+)([\\w/*?!.-]+)");

    private final Set<Path> files = new HashSet<>();

    public LocalFileLogStreamExtractor(String filePath) {
        fillFilesList(filePath);
    }

    public LocalFileLogStreamExtractor(String filePath, LocalDate trackingTime, boolean trackAfter) {
        super(trackingTime, trackAfter);
        fillFilesList(filePath);
    }

    public LocalFileLogStreamExtractor(String filePath, LocalDate trackingStartTime, LocalDate trackingEndTime) {
        super(trackingStartTime, trackingEndTime);
        fillFilesList(filePath);
    }

    @Override
    public Stream<LogRecord> extract() {
        return files.stream()
            .flatMap(path -> {
                List<LogRecord> logRecords = new ArrayList<>();
                try (BufferedReader reader = Files.newBufferedReader(path)) {
                    String line;
                    Matcher matcher;
                    while ((line = reader.readLine()) != null) {
                        matcher = NGINX_LOG_PATTERN.matcher(line);
                        if (matcher.find()) {
                            logRecords.add(parseLog(matcher));
                        }
                    }
                } catch (IOException e) {
                    LOGGER.error(String.format(CAUGHT_EXCEPTION_MESSAGE_TEMPLATE, e.getMessage()));
                }
                return logRecords
                    .stream()
                    .filter(timeLimitPredicate);
            });
    }

    @Override
    public String[] getSourceName() {
        return files
            .stream()
            .map(Path::getFileName)
            .map(Path::toString)
            .toArray(String[]::new);
    }

    private void fillFilesList(String filePath) {
        Matcher inputMatcher = FILE_PATH_INPUT_PATTERN.matcher(filePath);
        if (inputMatcher.find()) {
            Path parent = Path.of(inputMatcher.group(KNOWN_PATH_MATCHER_GROUP));
            DirectoryStream.Filter<Path> pathFilter = path -> {
                PathMatcher pathMatcher = FileSystems
                    .getDefault()
                    .getPathMatcher("glob:" + filePath);
                return pathMatcher.matches(path) && Files.isRegularFile(path);
            };
            try (
                DirectoryStream<Path> paths = Files.newDirectoryStream(parent, pathFilter)
            ) {
                paths.iterator().forEachRemaining(files::add);
            } catch (IOException e) {
                LOGGER.error(String.format(CAUGHT_EXCEPTION_MESSAGE_TEMPLATE, e.getMessage()));
            }
        }
        if (files.size() == 0) {
            throw new IllegalArgumentException("Unable to resolve path. Please, specify your path arguments.");
        }
    }

}
