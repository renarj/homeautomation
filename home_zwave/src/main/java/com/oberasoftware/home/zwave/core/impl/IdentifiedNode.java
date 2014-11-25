package com.oberasoftware.home.zwave.core.impl;

import com.oberasoftware.home.zwave.api.events.controller.NodeInformationEvent;
import com.oberasoftware.home.zwave.core.ZWaveNode;

/**
 * @author renarj
 */
public class IdentifiedNode implements ZWaveNode {

    private final int nodeId;
    private final NodeInformationEvent nodeInformation;

    public IdentifiedNode(int nodeId, NodeInformationEvent nodeInformation) {
        this.nodeId = nodeId;
        this.nodeInformation = nodeInformation;
    }

    @Override
    public int getNodeId() {
        return nodeId;
    }

    @Override
    public NodeInformationEvent getNodeInformation() {
        return nodeInformation;
    }
}
