package com.oberasoftware.home.nest;

import com.google.common.util.concurrent.Uninterruptibles;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
public class ConnectorTest {
    private static final Logger LOG = getLogger(ConnectorTest.class);

    public static void main(String[] args) {

        NestConnector connector = new NestConnector();
        connector.setAccessToken("c.899Hq9G1tFgCSizcgNHJbrCq7fl6F1tMHzjlIBvdVoYHxFxkuJtutcFepvfrTZNOMZA1V5IHWcC9ooRvng0qCH2H82K857oqKxbVLllONJDn8ElgWLmAolKEWVvL4uw2UXd0JYkh3B9WaTEu");

        LOG.debug("GOing to connect");
        connector.activate();

        while(!connector.isConnected()) {
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        }

        LOG.info("Thermostat connected");
        ThermostatManager theromostatManager = new ThermostatManager();
        theromostatManager.setNestConnector(connector);

        theromostatManager.getDevices().forEach(t -> LOG.debug("Thermostat: {}", t));
    }
}
