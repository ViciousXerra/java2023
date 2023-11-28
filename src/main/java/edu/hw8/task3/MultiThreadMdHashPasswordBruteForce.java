package edu.hw8.task3;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadMdHashPasswordBruteForce extends AbstractPasswordBruteForce {

    private final static int PREFERABLE_NUM_OF_THREADS = Runtime.getRuntime().availableProcessors();

    public MultiThreadMdHashPasswordBruteForce(Path path, int maxPasswordLength) {
        super(path, maxPasswordLength);
    }

    @Override
    public Map<String, String> extractPasswords() {
        Map<String, String> result = new ConcurrentHashMap<>();
        List<Runnable> runnables = getRunnableList(result);
        try (ExecutorService service = Executors.newFixedThreadPool(PREFERABLE_NUM_OF_THREADS)) {
            long start = System.currentTimeMillis();
            runnables.forEach(service::execute);
            service.shutdown();
            while (!service.isTerminated()) {

            }
            LOGGER.info(String.format(
                "%d threads. Estimated time: %d ms",
                PREFERABLE_NUM_OF_THREADS,
                System.currentTimeMillis() - start
            ));
        }
        return result;
    }

    private List<Runnable> getRunnableList(Map<String, String> result) {
        final Runnable retrievePasswordTask = () -> {
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
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error(String.format("Caught exception: %s", e.getMessage()));
            }
        };
        List<Runnable> runnablesList = new ArrayList<>(PREFERABLE_NUM_OF_THREADS);
        for (int i = 0; i < PREFERABLE_NUM_OF_THREADS; i++) {
            runnablesList.add(retrievePasswordTask);
        }
        return runnablesList;
    }

}
