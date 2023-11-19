package edu.project3.logstreamextractors;

import java.time.OffsetDateTime;

public record LogRecord(
    String remoteAddress, String remoteUser,
    OffsetDateTime localLogTime,
    RequestType requestType, String requestedResource,
    String status,
    long transferredBytes,
    String httpReferer, String userAgent
) {

    private final static String DEFAULT_STRING_VALUE = "-";

    public LogRecord() {
        this(
            DEFAULT_STRING_VALUE, DEFAULT_STRING_VALUE,
            OffsetDateTime.MIN,
            RequestType.UNKNOWN, DEFAULT_STRING_VALUE,
            String.valueOf(0),
            0,
            DEFAULT_STRING_VALUE, DEFAULT_STRING_VALUE
        );
    }

    public enum RequestType {
        GET,
        POST,
        PUT,
        PATCH,
        DELETE,
        UNKNOWN;

        public static RequestType resolveRequestType(String request) {
            return request.equals(GET.name()) ? GET
                : request.equals(POST.name()) ? POST
                : request.equals(PUT.name()) ? PUT
                : request.equals(PATCH.name()) ? PATCH
                : request.equals(DELETE.name()) ? DELETE
                : UNKNOWN;

        }
    }
}
