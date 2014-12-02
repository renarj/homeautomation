package com.oberasoftware.home.zwave.core.impl;

import com.oberasoftware.home.zwave.core.NodeStatus;
import com.oberasoftware.home.zwave.core.ZWaveNode;

/**
 * @author renarj
 */
public class BasicNode implements ZWaveNode {

    private final int nodeId;
    private final NodeStatus nodeStatus;

    public BasicNode(int nodeId) {
        this(nodeId, NodeStatus.INITIALIZING);
    }

    public BasicNode(int nodeId, NodeStatus nodeStatus) {
        this.nodeId = nodeId;
        this.nodeStatus = nodeStatus;
    }

    @Override
    public NodeStatus getNodeStatus() {
        return nodeStatus;
    }

    @Override
    public int getNodeId() {
        return nodeId;
    }
}
