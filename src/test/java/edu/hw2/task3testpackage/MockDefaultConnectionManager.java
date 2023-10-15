package edu.hw2.task3testpackage;

import edu.hw2.task3.Connection;
import edu.hw2.task3.DefaultConnectionManager;
import edu.hw2.task3.StableConnection;

class MockDefaultConnectionManager extends DefaultConnectionManager {

    private final boolean isFaulty;
    private final boolean faultyIsThrowing;

    MockDefaultConnectionManager(boolean isFaulty, boolean faultyIsThrowing) {
        this.isFaulty = isFaulty;
        this.faultyIsThrowing = faultyIsThrowing;
    }

    @Override
    public Connection getConnection() {

        if (isFaulty) {
            return new MockFaultyConnection(faultyIsThrowing);
        } else {
            return new StableConnection();
        }

    }

}
