package com.oberasoftware.home.experiment;

/**
 * @author renarj
 */
public class SensorInformation {
    private int nodeId;
    private int triggerCount;

    public SensorInformation(int nodeId, int triggerCount) {
        this.nodeId = nodeId;
        this.triggerCount = triggerCount;
    }

    public int getNodeId() {
        return nodeId;
    }

    public int getTriggerCount() {
        return triggerCount;
    }
}
