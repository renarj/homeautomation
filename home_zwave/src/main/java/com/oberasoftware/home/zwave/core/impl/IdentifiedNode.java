package com.oberasoftware.home.zwave.core.impl;

import com.oberasoftware.home.zwave.api.events.controller.NodeInformationEvent;
import com.oberasoftware.home.zwave.core.NodeStatus;
import com.oberasoftware.home.zwave.core.ZWaveNode;

/**
 * @author renarj
 */
public class IdentifiedNode implements ZWaveNode {

    private final int nodeId;
    private final NodeInformationEvent nodeInformation;

    private final NodeStatus nodeStatus;

    public IdentifiedNode(int nodeId, NodeInformationEvent nodeInformation, NodeStatus nodeStatus) {
        this.nodeId = nodeId;
        this.nodeInformation = nodeInformation;
        this.nodeStatus = nodeStatus;
    }

    public IdentifiedNode(int nodeId, NodeInformationEvent nodeInformation) {
        this(nodeId, nodeInformation, NodeStatus.IDENTIFIED);
    }

    @Override
    public int getNodeId() {
        return nodeId;
    }

    @Override
    public NodeStatus getNodeStatus() {
        return nodeStatus;
    }

    @Override
    public ZWaveNode setStatus(NodeStatus status) {
        return new IdentifiedNode(nodeId, nodeInformation, status);
    }

    @Override
    public NodeInformationEvent getNodeInformation() {
        return nodeInformation;
    }

    @Override
    public String toString() {
        return "IdentifiedNode{" +
                "nodeId=" + nodeId +
                ", nodeInformation=" + nodeInformation +
                ", nodeStatus=" + nodeStatus +
                '}';
    }
}
