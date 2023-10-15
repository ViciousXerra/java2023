package edu.hw2.task3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;

abstract class AbstractConnection implements Connection {

    private final static Logger LOGGER = LogManager.getLogger();

    public abstract void execute(@NotNull String message);

    @Override
    public void close() throws Exception {
        //Connection close code

        //Log connection close
        LOGGER.info("Connection closed");
    }

    public final void printLog(@NotBlank String message) {
        LOGGER.info(message);
    }

}
