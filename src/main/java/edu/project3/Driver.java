package edu.project3;

import edu.project3.logstreamextractors.LocalFileLogStreamExtractor;
import edu.project3.logstreamextractors.LogStreamExtractor;
import edu.project3.logstreamextractors.URLLogStreamExtractor;
import edu.project3.reportcomposers.LogStatReportComposer;
import edu.project3.reportcomposers.StatReportComposer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Driver {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String DEFAULT_SAVE_DIRECTORY_PATH = "src/test/resources/project3resources/demooutputs";

    private Driver() {

    }

    public static void execute(String[] args) {
        //if caller isn't a main method
        if (args == null || nullArgs(args)) {
            throw new IllegalArgumentException("Args array or args can't be null.");
        }
        if (args.length <= 1 || args.length % 2 != 0) {
            throw new IllegalArgumentException("Invalid args length.");
        } else if (!"--path".equals(args[0])) {
            throw new IllegalArgumentException("Args must be start with \"--path\" execution flag.");
        } else {
            tryLaunch(args);
        }
    }

    private static void tryLaunch(String[] args) {
        String source;
        boolean isLocal;
        LocalDate from = null;
        LocalDate to = null;
        LogStatReportComposer.ReportType format = LogStatReportComposer.ReportType.DEFAULT;
        if (args[1].isEmpty()) {
            LOGGER.error("File path argument can't be empty.");
            return;
        }
        source = args[1];
        isLocal = !args[1].startsWith("http");
        int ptr = 2;
        while (ptr < args.length) {
            if ("--from".equals(args[ptr])) {
                from = parseIsoDate(args[ptr + 1]);
            } else if ("--to".equals(args[ptr])) {
                to = parseIsoDate(args[ptr + 1]);
            } else if ("--format".equals(args[ptr])) {
                format = parseFormat(args[ptr + 1]);
            }
            ptr += 2;
        }
        LogStreamExtractor extractor;
        StatReportComposer composer;
        if (from == null && to == null) {
            extractor = getExtractor(source, isLocal);
            composer = new LogStatReportComposer(
                null,
                null,
                extractor.extract(),
                Arrays.stream(extractor.getSourceName()),
                format,
                DEFAULT_SAVE_DIRECTORY_PATH
            );
        } else if (from != null && to != null) {
            extractor = getExtractor(source, isLocal, from, to);
            composer = new LogStatReportComposer(
                from.toString(),
                to.toString(),
                extractor.extract(),
                Arrays.stream(extractor.getSourceName()),
                format,
                DEFAULT_SAVE_DIRECTORY_PATH
            );
        } else if (from != null) {
            extractor = getExtractor(source, isLocal, from, true);
            composer = new LogStatReportComposer(
                from.toString(),
                null,
                extractor.extract(),
                Arrays.stream(extractor.getSourceName()),
                format,
                DEFAULT_SAVE_DIRECTORY_PATH
            );
        } else {
            extractor = getExtractor(source, isLocal, to, false);
            composer = new LogStatReportComposer(
                null,
                to.toString(),
                extractor.extract(),
                Arrays.stream(extractor.getSourceName()),
                format,
                DEFAULT_SAVE_DIRECTORY_PATH
            );
        }
        composer.createReport();
    }

    private static LogStreamExtractor getExtractor(String source, boolean isLocal) {
        return isLocal ? new LocalFileLogStreamExtractor(source) : new URLLogStreamExtractor(source);
    }

    private static LogStreamExtractor getExtractor(String source, boolean isLocal, LocalDate start, LocalDate end) {
        return isLocal ? new LocalFileLogStreamExtractor(source, start, end)
            : new URLLogStreamExtractor(source, start, end);
    }

    private static LogStreamExtractor getExtractor(
        String source,
        boolean isLocal,
        LocalDate trackFrom,
        boolean isTrackAfter
    ) {
        return isLocal ? new LocalFileLogStreamExtractor(source, trackFrom, isTrackAfter)
            : new URLLogStreamExtractor(source, trackFrom, isTrackAfter);
    }

    private static LocalDate parseIsoDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private static LogStatReportComposer.ReportType parseFormat(String format) {
        if (format.equals("adoc")) {
            return LogStatReportComposer.ReportType.ADOC;
        } else if (format.equals("markdown")) {
            return LogStatReportComposer.ReportType.MARKDOWN;
        } else {
            return LogStatReportComposer.ReportType.DEFAULT;
        }
    }

    private static boolean nullArgs(String[] args) {
        for (String arg : args) {
            if (arg == null) {
                return true;
            }
        }
        return false;
    }

}
