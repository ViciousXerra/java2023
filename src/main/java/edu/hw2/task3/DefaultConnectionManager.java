package edu.hw2.task3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DefaultConnectionManager implements ConnectionManager {

    private final static Logger LOGGER = LogManager.getLogger();

    private final RandomEventHandler eventHandler;
    private final double throwingConnectionProbability;

    public DefaultConnectionManager(double faultyConnectionProbability, double throwingConnectionProbability) {
        eventHandler = new RandomEventHandler(faultyConnectionProbability);
        this.throwingConnectionProbability = throwingConnectionProbability;
    }

    @Override
    public Connection getConnection() {
        if (eventHandler.getEventOutcome()) {
            LOGGER.info("Stable connection accepted");
            return new StableConnection();
        } else {
            LOGGER.info("Faulty connection accepted");
            return new FaultyConnection(throwingConnectionProbability);
        }
    }

}
