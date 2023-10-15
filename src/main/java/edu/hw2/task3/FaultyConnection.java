package edu.hw2.task3;

import org.jetbrains.annotations.NotNull;

public class FaultyConnection extends AbstractConnection {

    private static int totalCount = 0;

    @Override
    public void execute(@NotNull String message) {
        //Attempt to establish connection with server

        /*
        Every second connection will throw ConnectionException
         */
        if (totalCount++ % 2 == 0) {
            throw new ConnectionException();
        }
        //Log printing
        printLog(String.format("Command \"%s\" execution", message));
        //Execution code

    }

}
