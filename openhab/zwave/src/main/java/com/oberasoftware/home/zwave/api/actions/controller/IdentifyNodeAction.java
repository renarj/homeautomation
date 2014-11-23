package com.oberasoftware.home.zwave.api.actions.controller;

import com.oberasoftware.home.zwave.api.ZWaveAction;

/**
 * @author renarj
 */
public class IdentifyNodeAction implements ZWaveAction {

    private int nodeId;

    public IdentifyNodeAction(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getNodeId() {
        return nodeId;
    }
}
