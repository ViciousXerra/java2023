package edu.project3.logstreamextractors;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import static edu.project3.logstreamextractors.LogRecord.RequestType.resolveRequestType;
import static edu.project3.logstreamextractors.LogUtils.NGINX_HTTP_REFERER_MATCHER_GROUP;
import static edu.project3.logstreamextractors.LogUtils.NGINX_LOCAL_TIME_MATCHER_GROUP;
import static edu.project3.logstreamextractors.LogUtils.NGINX_REMOTE_ADDRESS_MATCHER_GROUP;
import static edu.project3.logstreamextractors.LogUtils.NGINX_REMOTE_USER_MATCHER_GROUP;
import static edu.project3.logstreamextractors.LogUtils.NGINX_REQUEST_RESOURCE_MATCHER_GROUP;
import static edu.project3.logstreamextractors.LogUtils.NGINX_REQUEST_TYPE_MATCHER_GROUP;
import static edu.project3.logstreamextractors.LogUtils.NGINX_STATUS_MATCHER_GROUP;
import static edu.project3.logstreamextractors.LogUtils.NGINX_TRANSFERRED_BYTES_MATCHER_GROUP;
import static edu.project3.logstreamextractors.LogUtils.NGINX_USER_AGENT_MATCHER_GROUP;
import static edu.project3.logstreamextractors.LogUtils.OFFSET_DATE_TIME_FORMATTER;

public abstract class AbstractLogStreamExtractor implements LogStreamExtractor {

    protected Predicate<LogRecord> timeLimitPredicate;

    protected AbstractLogStreamExtractor() {
        timeLimitPredicate = logRecord -> true;
    }

    protected AbstractLogStreamExtractor(LocalDate trackingTime, boolean trackAfter) {
        OffsetDateTime trackFrom = OffsetDateTime.of(
            trackingTime,
            LocalTime.MIDNIGHT,
            ZoneOffset.UTC
        );
        if (trackAfter) {
            timeLimitPredicate = logRecord -> trackFrom.isBefore(logRecord.localLogTime());
        } else {
            timeLimitPredicate = logRecord -> trackFrom.isAfter(logRecord.localLogTime());
        }
    }

    protected AbstractLogStreamExtractor(LocalDate trackingStartTime, LocalDate trackingEndTime) {
        OffsetDateTime startFrom = OffsetDateTime.of(
            trackingStartTime,
            LocalTime.MIDNIGHT,
            ZoneOffset.UTC
        );
        OffsetDateTime endTo = OffsetDateTime.of(
            trackingEndTime,
            LocalTime.MIDNIGHT,
            ZoneOffset.UTC
        );
        timeLimitPredicate =
            logRecord -> startFrom.isBefore(logRecord.localLogTime()) && endTo.isAfter(logRecord.localLogTime());
    }

    protected LogRecord parseLog(Matcher matcher) {
        return new LogRecord(
            matcher.group(NGINX_REMOTE_ADDRESS_MATCHER_GROUP),
            matcher.group(NGINX_REMOTE_USER_MATCHER_GROUP),
            OffsetDateTime.parse(
                matcher.group(NGINX_LOCAL_TIME_MATCHER_GROUP),
                OFFSET_DATE_TIME_FORMATTER
            ),
            resolveRequestType(matcher.group(NGINX_REQUEST_TYPE_MATCHER_GROUP)),
            matcher.group(NGINX_REQUEST_RESOURCE_MATCHER_GROUP),
            matcher.group(NGINX_STATUS_MATCHER_GROUP),
            Long.parseLong(matcher.group(NGINX_TRANSFERRED_BYTES_MATCHER_GROUP)),
            matcher.group(NGINX_HTTP_REFERER_MATCHER_GROUP),
            matcher.group(NGINX_USER_AGENT_MATCHER_GROUP)
        );
    }

}
