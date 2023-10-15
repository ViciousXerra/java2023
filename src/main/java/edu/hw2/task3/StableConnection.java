package edu.hw2.task3;

import org.jetbrains.annotations.NotNull;

public class StableConnection extends AbstractConnection {

    @Override
    public void execute(@NotNull String message) {
        //Attempt to establish connection with server

        //Log printing
        printLog(String.format("Command \"%s\" execution", message));
        //Execution code

    }

}
