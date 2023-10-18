package edu.hw2.task3;

import org.jetbrains.annotations.NotNull;

public final class PopularCommandExecutor {

    private final ConnectionManager manager;
    private final int maxAttempts;

    public PopularCommandExecutor(@NotNull ConnectionManager manager, int maxAttempts) {
        this.manager = manager;
        this.maxAttempts = maxAttempts;
    }

    public void updatePackages() {
        tryExecute("apt update && apt upgrade -y");
    }

    public void tryExecute(@NotNull String command) {
        Connection con = manager.getConnection();
        Retryer<String, Connection> retryer = new Retryer<>(command, con);
        try {
            retryer.proceed(con::execute, maxAttempts);
        } catch (Exception e) {
            throw new ConnectionException("Exceeding the maximum number of attempts", (ConnectionException) e);
        }
    }

}
