package com.oberasoftware.home.zwave.core;


import java.util.List;

/**
 * @author renarj
 */
public interface NodeManager {
    void registerNode(int nodeId);

    void markDead(int nodeId);

    NodeStatus getNodeStatus(int nodeId);

    ZWaveNode getNode(int nodeId);

    List<ZWaveNode> getNodes();
}
