package com.oberasoftware.home.zwave.api.actions.controller;

import com.oberasoftware.home.zwave.api.ZWaveDeviceAction;

/**
 * @author renarj
 */
public class NodeNoOpAction implements ZWaveDeviceAction {
    private int nodeId;

    public NodeNoOpAction(int nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public int getNodeId() {
        return nodeId;
    }
}
