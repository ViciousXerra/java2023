package edu.hw2.task3;

import org.jetbrains.annotations.NotNull;

public final class PopularCommandExecutor {

    private final ConnectionManager manager;
    private final int maxAttempts;

    public PopularCommandExecutor(@NotNull ConnectionManager manager, int maxAttempts) {
        this.manager = manager;
        this.maxAttempts = maxAttempts;
    }

    public void updatePackages() throws Exception {
        tryExecute("apt update && apt upgrade -y");
    }

    public void tryExecute(@NotNull String command) throws Exception {
        Connection con = manager.getConnection();
        for (int i = 1; i <= maxAttempts; i++) {
            try (con) {
                con.execute(command);
                break;
            } catch (ConnectionException e) {
                if (i == maxAttempts) {
                    throw new ConnectionException("Exceeding the maximum number of attempts", e);
                }
            }
        }
    }

}
