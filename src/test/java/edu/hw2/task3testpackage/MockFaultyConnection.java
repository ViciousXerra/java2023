package edu.hw2.task3testpackage;

import edu.hw2.task3.ConnectionException;
import edu.hw2.task3.FaultyConnection;
import org.jetbrains.annotations.NotNull;

class MockFaultyConnection extends FaultyConnection {

    private final boolean isThrowing;

    MockFaultyConnection(boolean isThrowing) {
        this.isThrowing = isThrowing;
    }

    @Override
    public void execute(@NotNull String message) {
        if (isThrowing) {
            throw new ConnectionException();
        } else {
            //Log printing
            printLog(String.format("Command \"%s\" execution", message));
            //Execution code
        }
    }
}
