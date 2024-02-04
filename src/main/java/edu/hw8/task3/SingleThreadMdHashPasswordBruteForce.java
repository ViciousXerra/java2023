package edu.hw8.task3;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SingleThreadMdHashPasswordBruteForce extends AbstractPasswordBruteForce {

    public SingleThreadMdHashPasswordBruteForce(Path path, int maxPasswordLength) {
        super(path, maxPasswordLength);
    }

    public Map<String, String> extractPasswords() {
        Map<String, String> result = new HashMap<>();
        long start = System.currentTimeMillis();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String key;
            byte[] hash;
            String password;
            String employee;
            while (!employeeAndHashedPassword.isEmpty() && generator.hasNextPassword()) {
                password = generator.nextPassword();
                hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
                key = toHex(hash);
                employee = employeeAndHashedPassword.remove(key);
                if (employee == null) {
                    continue;
                }
                result.put(employee, password);
                LOGGER.info(String.format("%s password cracked.", employee));
            }
            LOGGER.info(String.format("Single thread. Estimated time: %d ms", System.currentTimeMillis() - start));
            return result;
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(String.format("Caught exception: %s", e.getMessage()));
        }
        return Map.of();
    }

}
