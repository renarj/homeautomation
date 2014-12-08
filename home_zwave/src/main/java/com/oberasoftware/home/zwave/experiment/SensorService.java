package com.oberasoftware.home.zwave.experiment;

import com.oberasoftware.home.zwave.api.events.EventListener;
import com.oberasoftware.home.zwave.api.events.Subscribe;
import com.oberasoftware.home.zwave.api.events.devices.BasicEvent;
import com.oberasoftware.home.zwave.api.events.devices.BatteryEvent;
import com.oberasoftware.home.zwave.api.events.devices.BinarySensorEvent;
import com.oberasoftware.home.zwave.api.events.devices.DeviceSensorEvent;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Serie;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author renarj
 */
@Component
public class SensorService implements EventListener<BinarySensorEvent> {

    private Map<Integer, SensorInformation> nodeSensorData = new ConcurrentHashMap<>();

    @Override
    public void receive(BinarySensorEvent event) throws Exception {
        if(event.isTriggered()) {
            ensureNodeSensorData(event.getNodeId());

            nodeSensorData.get(event.getNodeId()).incrementTriggerCount();
        }
    }

    @Subscribe
    public void receive(DeviceSensorEvent event) throws Exception {
        int nodeId = event.getNodeId();
        ensureNodeSensorData(nodeId);

        if(event.containsValue()) {
            nodeSensorData.get(nodeId).setSensorData(event.getSensorType().getLabel(), event.getValue());

            sendInfluxDBPower(event.getNodeId(), event.getSensorType().getLabel(), event.getValue().doubleValue());
        }
    }

    @Subscribe
    public void receive(BatteryEvent batteryEvent) throws Exception {
        int nodeId = batteryEvent.getNodeId();
        ensureNodeSensorData(nodeId);

        nodeSensorData.get(nodeId).setSensorData("Battery", new BigDecimal(batteryEvent.getBatteryLevel()));

        sendInfluxDBPower(nodeId, "Battery", batteryEvent.getBatteryLevel());
    }

    @Subscribe
    public void receiveBasic(BasicEvent basicEvent) throws Exception {
        int nodeId = basicEvent.getNodeId();
        ensureNodeSensorData(nodeId);

        if(basicEvent.getNodeId() == 12 && basicEvent.getValue() != 0x00) {
            nodeSensorData.get(nodeId).incrementTriggerCount();
        }
    }

    private void sendInfluxDBPower(int nodeId, String sensorType, double value) {
        InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086", "root", "root");

        Serie powerSerie = new Serie.Builder("sensor_value")
                .columns("time", "node", "sensor", "value")
                .values(System.currentTimeMillis(), nodeId, sensorType, value)
                .build();

        influxDB.write("sensor_data", TimeUnit.MILLISECONDS, powerSerie);
    }

    private void ensureNodeSensorData(int nodeId) {
        nodeSensorData.computeIfAbsent(nodeId, v -> new SensorInformation(nodeId, 0));
    }

    public Map<Integer, SensorInformation> getSensorInformation() {
        return new HashMap<>(nodeSensorData);
    }

    public int getCount(int nodeId) {
        return nodeSensorData.get(nodeId).getTriggerCount();
    }
}
