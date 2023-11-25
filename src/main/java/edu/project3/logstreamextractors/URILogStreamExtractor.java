package edu.project3.logstreamextractors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Stream;
import javax.net.ssl.HttpsURLConnection;
import static edu.project3.logstreamextractors.LogUtils.NGINX_LOG_PATTERN;

public class URILogStreamExtractor extends AbstractLogStreamExtractor {

    private final URI resource;
    private final String body;

    public URILogStreamExtractor(URI resource) {
        this.resource = resource;
        try {
            if (resource.toString().startsWith("http")) {
                body = getHttpResponseBody(resource);
            } else {
                body = getFileResponseBody(resource);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public URILogStreamExtractor(URI resource, LocalDate trackingTime, boolean trackAfter) {
        super(trackingTime, trackAfter);
        this.resource = resource;
        try {
            if (resource.toString().startsWith("http")) {
                body = getHttpResponseBody(resource);
            } else {
                body = getFileResponseBody(resource);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public URILogStreamExtractor(URI resource, LocalDate trackingStartTime, LocalDate trackingEndTime) {
        super(trackingStartTime, trackingEndTime);
        this.resource = resource;
        try {
            if (resource.toString().startsWith("http")) {
                body = getHttpResponseBody(resource);
            } else {
                body = getFileResponseBody(resource);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Stream<LogRecord> extract() {
        List<LogRecord> logRecords = new ArrayList<>();
        Matcher matcher = NGINX_LOG_PATTERN.matcher(body);
        while (matcher.find()) {
            logRecords.add(parseLog(matcher));
        }
        return logRecords
            .stream()
            .filter(timeLimitPredicate);
    }

    @Override
    public String[] getSourceName() {
        return new String[] {resource.toString()};
    }

    private static String getHttpResponseBody(URI resource) throws IOException {
        HttpsURLConnection httpClient = (HttpsURLConnection) resource.toURL().openConnection();
        httpClient.setRequestMethod("GET");
        try (BufferedReader in = new BufferedReader(
            new InputStreamReader(httpClient.getInputStream()))) {

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {
                response.append(line);
                response.append(System.lineSeparator());
            }
            return response.toString();
        }
    }

    private static String getFileResponseBody(URI resource) throws IOException {
        try (BufferedReader in = new BufferedReader(
            new InputStreamReader(resource.toURL().openConnection().getInputStream()))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = in.readLine()) != null) {
                response.append(line);
                response.append(System.lineSeparator());
            }
            return response.toString();
        }
    }

}
