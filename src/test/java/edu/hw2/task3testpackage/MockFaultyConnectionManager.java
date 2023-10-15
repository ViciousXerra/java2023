package edu.hw2.task3testpackage;

import edu.hw2.task3.Connection;
import edu.hw2.task3.FaultyConnectionManager;

class MockFaultyConnectionManager extends FaultyConnectionManager {

    private final boolean connectionIsThrowing;

    MockFaultyConnectionManager(boolean connectionIsThrowing) {
        this.connectionIsThrowing = connectionIsThrowing;
    }

    @Override
    public Connection getConnection() {
        return new MockFaultyConnection(connectionIsThrowing);
    }

}
