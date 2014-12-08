package com.oberasoftware.home.experiment;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author renarj
 */
public class SensorInformation {
    private int nodeId;
    private int triggerCount;

    private Map<String, BigDecimal> sensorData = new HashMap<>();

    public SensorInformation(int nodeId, int triggerCount) {
        this.nodeId = nodeId;
        this.triggerCount = triggerCount;
    }

    public void incrementTriggerCount() {
        this.triggerCount++;
    }

    public int getNodeId() {
        return nodeId;
    }

    public int getTriggerCount() {
        return triggerCount;
    }

    public Map<String, BigDecimal> getSensorData() {
        return sensorData;
    }

    public void setSensorData(String sensor, BigDecimal value) {
        sensorData.put(sensor, value);
    }
}
