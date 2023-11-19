package edu.project3.logstreamextractors;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.regex.Pattern;

public final class LogUtils {

    public final static DateTimeFormatter OFFSET_DATE_TIME_FORMATTER =
        new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("dd/LLL/uuuu:HH:mm:ss Z")
            .toFormatter(Locale.ENGLISH);

    public final static int NGINX_REMOTE_ADDRESS_MATCHER_GROUP = 1;
    public final static int NGINX_REMOTE_USER_MATCHER_GROUP = 3;
    public final static int NGINX_LOCAL_TIME_MATCHER_GROUP = 4;
    public final static int NGINX_REQUEST_TYPE_MATCHER_GROUP = 5;
    public final static int NGINX_REQUEST_RESOURCE_MATCHER_GROUP = 6;
    public final static int NGINX_STATUS_MATCHER_GROUP = 7;
    public final static int NGINX_TRANSFERRED_BYTES_MATCHER_GROUP = 8;
    public final static int NGINX_HTTP_REFERER_MATCHER_GROUP = 9;
    public final static int NGINX_USER_AGENT_MATCHER_GROUP = 10;

    public final static Pattern NGINX_LOG_PATTERN =
        Pattern.compile(
            "((\\d{1,3}\\.){3}\\d{1,3}) - (.+) " +
                "\\[(.+)] \"(GET|POST|PUT|PATCH|DELETE) (.+) HTTP/\\d\\.\\d\" " +
                "(\\d{3}) (\\d+) " +
                "\"(.+)\" \"(.+)\""
        );

    private LogUtils() {

    }

}
