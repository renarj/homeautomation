package com.oberasoftware.home.zwave.core.impl;

import com.oberasoftware.home.zwave.core.ZWaveNode;

/**
 * @author renarj
 */
public class BasicNode implements ZWaveNode {

    private final int nodeId;

    public BasicNode(int nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public int getNodeId() {
        return nodeId;
    }
}
