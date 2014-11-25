package com.oberasoftware.home.zwave.core;


import com.oberasoftware.home.zwave.api.events.controller.NodeInformationEvent;

import java.util.List;

/**
 * @author renarj
 */
public interface NodeManager {
    void registerNode(int nodeId);

    void markDead(int nodeId);

    NodeStatus getNodeStatus(int nodeId);

    ZWaveNode getNode(int nodeId);

    void setNodeInformation(int nodeId, NodeInformationEvent nodeInformationEvent);

    List<ZWaveNode> getNodes();
}
