package edu.hw2.task3;

public class ConnectionException extends RuntimeException {

    public ConnectionException() {

    }

    public ConnectionException(String message, ConnectionException e) {
        super(message, e);
    }

}
