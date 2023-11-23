package edu.project3.logstreamextractors;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

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

        private final static Map<String, RequestType> TYPE_MAPPING = new HashMap<>() {
            {
                put("GET", RequestType.GET);
                put("POST", RequestType.POST);
                put("PUT", RequestType.PUT);
                put("PATCH", RequestType.PATCH);
                put("DELETE", RequestType.DELETE);
            }
        };

        public static RequestType resolveRequestType(String request) {
            return TYPE_MAPPING.getOrDefault(request, RequestType.UNKNOWN);
        }

    }

}
