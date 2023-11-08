package edu.hw5.task3.instanceschainbuilder;

public class ZeroLengthChainException extends RuntimeException {

    private final static String MESSAGE = "Chain has length of 0.";

    ZeroLengthChainException() {
        super(MESSAGE);
    }

}
