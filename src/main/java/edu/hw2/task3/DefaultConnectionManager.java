package edu.hw2.task3;

public class DefaultConnectionManager implements ConnectionManager {

    private static int totalRetrieveCount = 0;

    @Override
    public Connection getConnection() {
        /*
        Every second retrieve will be FaultyConnection
         */
        if (totalRetrieveCount++ % 2 == 0) {
            return new FaultyConnection();
        } else {
            return new StableConnection();
        }
    }

}
