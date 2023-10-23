package edu.hw2.task3;

import org.jetbrains.annotations.NotNull;

public class FaultyConnection extends AbstractConnection {

    private final RandomEventHandler eventHandler;

    public FaultyConnection(double throwingConnectionProbability) {
        eventHandler = new RandomEventHandler(throwingConnectionProbability);
    }

    @Override
    public void execute(@NotNull String message) {

        if (!eventHandler.getEventOutcome()) {
            throw new ConnectionException();
        }
        //Log printing
        printLog(String.format("Command \"%s\" execution", message));
        //Execution code

    }

}
