package edu.project3.logstreamextractors;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static edu.project3.logstreamextractors.LogUtils.NGINX_LOG_PATTERN;

public class URLLogStreamExtractor extends AbstractLogStreamExtractor {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String CAUGHT_EXCEPTION_MESSAGE_TEMPLATE = "Caught exception: {%s}";

    private final HttpRequest request;
    private final String resource;

    public URLLogStreamExtractor(String resource) {
        this.resource = resource;
        request = getRequestInstance(resource);
    }

    public URLLogStreamExtractor(String resource, LocalDate trackingTime, boolean trackAfter) {
        super(trackingTime, trackAfter);
        this.resource = resource;
        request = getRequestInstance(resource);
    }

    public URLLogStreamExtractor(String resource, LocalDate trackingStartTime, LocalDate trackingEndTime) {
        super(trackingStartTime, trackingEndTime);
        this.resource = resource;
        request = getRequestInstance(resource);
    }

    @Override
    public Stream<LogRecord> extract() {
        HttpResponse<String> response;
        List<LogRecord> logRecords = new ArrayList<>();
        try (
            HttpClient client = getHttpClientInstance()
        ) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            Matcher matcher = NGINX_LOG_PATTERN.matcher(body);
            while (matcher.find()) {
                logRecords.add(parseLog(matcher));
            }
            return logRecords
                .stream()
                .filter(timeLimitPredicate);
        } catch (IOException | InterruptedException e) {
            LOGGER.error(String.format(CAUGHT_EXCEPTION_MESSAGE_TEMPLATE, e.getMessage()));
        }
        return Stream.of();
    }

    @Override
    public String[] getSourceName() {
        return new String[] {resource};
    }

    private static HttpRequest getRequestInstance(String uriString) {
        return HttpRequest
            .newBuilder()
            .uri(URI.create(uriString))
            .version(HttpClient.Version.HTTP_2)
            .GET()
            .build();
    }

    private static HttpClient getHttpClientInstance() {
        return HttpClient
            .newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    }

}
