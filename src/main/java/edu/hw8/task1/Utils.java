package edu.hw8.task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class Utils {
    private final static Logger LOGGER = LogManager.getLogger();
    private final static Path SOURCE_FILE_PATH = Path.of("src/main/resources/hw8resources/quotes.txt");
    private final static Pattern PATTERN = Pattern.compile("^([А-яЕё]+):([А-яЕё -.]+)$");
    private final static int KEYWORD_GROUP = 1;
    private final static int QUOTE_GROUP = 2;
    //Only for reading
    private final static Map<String, Set<String>> KEYWORD_MAPPING = new HashMap<>() {
        {
            try (BufferedReader reader = Files.newBufferedReader(SOURCE_FILE_PATH)) {
                String line;
                Matcher matcher;
                while ((line = reader.readLine()) != null) {
                    matcher = PATTERN.matcher(line);
                    if (matcher.find()) {
                        Set<String> set = getOrDefault(matcher.group(KEYWORD_GROUP), new HashSet<>());
                        if (set.isEmpty()) {
                            put(matcher.group(KEYWORD_GROUP), set);
                        }
                        set.add(matcher.group(QUOTE_GROUP));
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Unable to read source file.");
            }
        }
    };

    private Utils() {

    }

    public static String getSuitableQuotes(String keyword) {
        StringBuilder builder = new StringBuilder();
        KEYWORD_MAPPING.getOrDefault(keyword, Set.of("There is no existing suitable quotes."))
            .forEach(quote -> {
                builder.append(quote);
                builder.append(System.lineSeparator());
            });
        return new String(builder.toString().getBytes(), StandardCharsets.UTF_8);
    }

    public static int getFreePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

}
