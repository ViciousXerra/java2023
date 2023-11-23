package edu.project3.reportcomposers;

import edu.project3.logstreamextractors.LogRecord;
import edu.project3.reportcomposers.statcontentcreators.AdocStatContentGenerator;
import edu.project3.reportcomposers.statcontentcreators.MarkDownStatContentGenerator;
import edu.project3.reportcomposers.statcontentcreators.StatContentCreator;
import edu.project3.reportcomposers.statcontentcreators.TxtStatContentGenerator;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogStatReportComposer implements StatReportComposer {

    private final static Logger LOGGER = LogManager.getLogger();

    private final StatContentCreator contentCreator;
    private final String directoryToSave;

    public LogStatReportComposer(
        String trackingStartTime,
        String trackingEndTime,
        Stream<LogRecord> logStream,
        Stream<String> sourceNames,
        String format,
        String directoryToSave
    ) {
        StatContentCreator.ReportType type = StatContentCreator.ReportType.resolveReportType(format);
        this.contentCreator = getCreator(
            type,
            trackingStartTime,
            trackingEndTime,
            logStream,
            sourceNames
        );
        this.directoryToSave = directoryToSave;
    }

    @Override
    public void createReport() {
        contentCreator.collectStat();
        try (BufferedWriter writer = Files.newBufferedWriter(
            Path.of(directoryToSave, contentCreator.getFileName()),
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.TRUNCATE_EXISTING
        )) {
            String formattedOutput = contentCreator.generateContent();
            writer.write(formattedOutput);
            LOGGER.info("See " + Path.of(directoryToSave, contentCreator.getFileName()) + " for results.");
        } catch (IOException e) {
            LOGGER.error(String.format("Unable to save output. Caught exception: {%s}", e.getMessage()));
        }
    }

    private static StatContentCreator getCreator(
        StatContentCreator.ReportType format,
        String trackingStartTime,
        String trackingEndTime,
        Stream<LogRecord> logStream,
        Stream<String> sourceNames
    ) {
        return switch (format) {
            case DEFAULT -> new TxtStatContentGenerator(
                trackingStartTime,
                trackingEndTime,
                logStream,
                sourceNames
            );
            case MARKDOWN -> new MarkDownStatContentGenerator(
                trackingStartTime,
                trackingEndTime,
                logStream,
                sourceNames
            );
            case ADOC -> new AdocStatContentGenerator(
                trackingStartTime,
                trackingEndTime,
                logStream,
                sourceNames
            );
        };
    }

}
