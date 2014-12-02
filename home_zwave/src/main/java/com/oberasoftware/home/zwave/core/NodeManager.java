package com.oberasoftware.home.zwave.core;


import com.oberasoftware.home.zwave.api.events.controller.NodeInformationEvent;

import java.util.List;

/**
 * @author renarj
 */
public interface NodeManager {
    void registerNode(int nodeId);

    void registerNode(ZWaveNode node);

    void markDead(int nodeId);

    /**
     * This indicates if all nodes in the network have reached the minimal status specified
     * @param nodeStatus The minimal status that all nodes should have achieved
     * @return True if all nodes have achieved this minimal status
     */
    boolean haveNodeMinimalStatus(NodeStatus nodeStatus);

    NodeStatus getNodeStatus(int nodeId);

    ZWaveNode setNodeStatus(int nodeId, NodeStatus nodeStatus);

    ZWaveNode getNode(int nodeId);

    void setNodeInformation(int nodeId, NodeInformationEvent nodeInformationEvent);

    List<ZWaveNode> getNodes();
}
