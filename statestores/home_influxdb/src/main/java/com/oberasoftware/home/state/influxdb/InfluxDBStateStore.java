package com.oberasoftware.home.state.influxdb;

import com.oberasoftware.home.api.exceptions.RuntimeHomeAutomationException;
import com.oberasoftware.home.api.managers.StateStore;
import com.oberasoftware.home.api.model.State;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Serie;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class InfluxDBStateStore implements StateStore {
    private static final Logger LOG = getLogger(InfluxDBStateStore.class);

    @Value("${influxdb.host}")
    private String host;

    @Value("${influxdb.user}")
    private String user;

    @Value("${influxdb.password}")
    private String password;

    @Value("${influxdb.database}")
    private String database;

    @Value("${influxdb.port}")
    private int port;


    private InfluxDB influxDB;

    @PostConstruct
    public void initialise() {
        LOG.info("Connecting to InfluxDB on host: {} and port: {}", host, port);

        String connectionString = String.format("http://%s:%d", host, port);

        this.influxDB = InfluxDBFactory.connect(connectionString, user, password);
    }

    @Override
    public void store(String itemId, String controllerId, String pluginId, String deviceId, String label, com.oberasoftware.home.api.types.Value value) {
        LOG.debug("Writing time series: {} to InfluxDB", controllerId);

        double convertedValue = 0;
        switch(value.getType()) {
            case NUMBER:
                Object v = value.getValue();
                if(v instanceof Long) {
                    convertedValue = value.<Long>getValue();
                } else if (v instanceof Integer) {
                    convertedValue = value.<Integer>getValue();
                }
                break;
            case DECIMAL:
                convertedValue = value.getValue();
                break;
            case STRING:
            default:
                if(value.asString().equals("on")) {
                    convertedValue = 1.0;
                }
        }

        Serie serie = new Serie.Builder(controllerId)
                .columns("time", "itemId", "pluginId", "deviceId", "label", "value")
                .values(System.currentTimeMillis(), itemId, pluginId, deviceId, label, convertedValue)
                .build();

        this.influxDB.write(database, TimeUnit.MILLISECONDS, serie);
    }

    @Override
    public Map<String, State> getStates() {
        throw new RuntimeHomeAutomationException("Retrieval of state from InfluxDB not supported");
    }

    @Override
    public State getState(String itemId) {
        throw new RuntimeHomeAutomationException("Retrieval of state from InfluxDB not supported");
    }

    @Override
    public SUPPORTED_OPERATIONS getSupportedOperations() {
        return SUPPORTED_OPERATIONS.WRITE;
    }
}
