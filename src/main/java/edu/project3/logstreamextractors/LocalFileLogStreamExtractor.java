package edu.project3.logstreamextractors;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static edu.project3.logstreamextractors.LogUtils.NGINX_LOG_PATTERN;

public class LocalFileLogStreamExtractor extends AbstractLogStreamExtractor {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String CAUGHT_EXCEPTION_MESSAGE_TEMPLATE = "Caught exception: {%s}";

    private final static int KNOWN_PATH_MATCHER_GROUP = 1;
    private final static int GLOBBING_PATTERN_MATCHER_GROUP = 3;
    private final static Pattern FILE_PATH_INPUT_PATTERN =
        Pattern.compile("(([\\w()]+/)+)([\\w/*?!.-]+)");

    private Iterable<Path> files;

    public LocalFileLogStreamExtractor(String filePath) {
        fillFilesList(filePath);
    }

    public LocalFileLogStreamExtractor(String filePath, String trackingStartTime) {
        super(trackingStartTime);
        fillFilesList(filePath);
    }

    public LocalFileLogStreamExtractor(String filePath, String trackingStartTime, String trackingEndTime) {
        super(trackingStartTime, trackingEndTime);
        fillFilesList(filePath);
    }

    @Override
    public Stream<LogRecord> extract() {
        return StreamSupport
            .stream(files.spliterator(), false)
            .flatMap(path -> {
                List<LogRecord> logRecords = new ArrayList<>();
                try (BufferedReader reader = Files.newBufferedReader(path)) {
                    Matcher matcher;
                    while (reader.ready()) {
                        matcher = NGINX_LOG_PATTERN.matcher(reader.readLine());
                        if (matcher.find()) {
                            logRecords.add(parseLog(matcher));
                        }
                    }
                } catch (IOException e) {
                    LOGGER.error(String.format(CAUGHT_EXCEPTION_MESSAGE_TEMPLATE, e.getMessage()));
                }
                return logRecords.stream();
            })
            .filter(timeLimitPredicate);
    }

    @Override
    public String[] getSourceName() {
        return (String[]) StreamSupport
            .stream(files.spliterator(), false)
            .map(Path::getFileName)
            .map(Object::toString)
            .toArray();
    }

    private void fillFilesList(String filePath) {
        Matcher inputMatcher = FILE_PATH_INPUT_PATTERN.matcher(filePath);
        if (inputMatcher.find()) {
            Path parent = Path.of("/" + inputMatcher.group(KNOWN_PATH_MATCHER_GROUP));
            DirectoryStream.Filter<Path> pathFilter = path -> {
                PathMatcher pathMatcher = FileSystems
                    .getDefault()
                    .getPathMatcher("glob:" + inputMatcher.group(
                        GLOBBING_PATTERN_MATCHER_GROUP));
                return pathMatcher.matches(path) && Files.isRegularFile(path);
            };
            try (
                DirectoryStream<Path> paths = Files.newDirectoryStream(parent, pathFilter)
            ) {
                files = paths;
            } catch (IOException e) {
                LOGGER.error(String.format(CAUGHT_EXCEPTION_MESSAGE_TEMPLATE, e.getMessage()));
            }
        } else {
            throw new IllegalArgumentException("Unable to resolve path. Please, specify your path arguments.");
        }
    }

}
