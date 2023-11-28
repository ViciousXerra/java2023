package edu.hw8.task3;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract class AbstractPasswordBruteForce implements PasswordBruteForce {

    protected final static Logger LOGGER = LogManager.getLogger();
    protected final static Pattern PATTERN = Pattern.compile("([A-Za-z]\\.[A-Za-z]\\.[A-Za-z]+) +(\\w+)");
    protected final static int EMPLOYEE_NAME_GROUP = 1;
    protected final static int HASH_MD5_PASSWORD_GROUP = 2;
    private final static int MASK = 0xFF;
    protected final Map<String, String> employeeAndHashedPassword;
    protected final PasswordGenerator generator;

    protected AbstractPasswordBruteForce(Path path, int maxPasswordLength) {
        employeeAndHashedPassword = new HashMap<>();
        generator = new PasswordGenerator(maxPasswordLength);
        if (path == null) {
            throw new IllegalArgumentException("Path can't be null.");
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            Matcher matcher;
            String line;
            while ((line = reader.readLine()) != null) {
                matcher = PATTERN.matcher(line);
                if (matcher.find()) {
                    employeeAndHashedPassword.put(
                        matcher.group(HASH_MD5_PASSWORD_GROUP),
                        matcher.group(EMPLOYEE_NAME_GROUP)
                    );
                }
            }
        } catch (IOException e) {
            LOGGER.error(String.format("Caught exception: %s", e.getMessage()));
        }
    }

    protected static String toHex(byte[] hash) {
        StringBuilder builder = new StringBuilder();
        String hex;
        for (byte b : hash) {
            hex = Integer.toHexString(b & MASK);
            if (hex.length() == 1) {
                builder.append(0);
            }
            builder.append(hex);
        }
        return builder.toString();
    }

}
