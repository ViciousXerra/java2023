package edu.hw2.task3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FaultyConnectionManager implements ConnectionManager {

    private final static Logger LOGGER = LogManager.getLogger();

    private final double throwingConnectionProbability;

    public FaultyConnectionManager(double throwingConnectionProbability) {
        this.throwingConnectionProbability = throwingConnectionProbability;
    }

    @Override
    public Connection getConnection() {
        LOGGER.info("Faulty connection accepted");
        return new FaultyConnection(throwingConnectionProbability);
    }

}
