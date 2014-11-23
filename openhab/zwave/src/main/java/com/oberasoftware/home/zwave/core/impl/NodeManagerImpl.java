package com.oberasoftware.home.zwave.core.impl;

import com.oberasoftware.home.zwave.core.NodeManager;
import com.oberasoftware.home.zwave.core.NodeStatus;
import com.oberasoftware.home.zwave.core.ZWaveNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author renarj
 */
@Component
public class NodeManagerImpl implements NodeManager {

    private Map<Integer, ZWaveNode> nodeMap = new ConcurrentHashMap<>();

    @Override
    public void registerNode(int nodeId) {
        nodeMap.putIfAbsent(nodeId, new ZWaveNodeImpl(nodeId));
    }

    @Override
    public void markDead(int nodeId) {

    }

    @Override
    public NodeStatus getNodeStatus(int nodeId) {
        return null;
    }

    @Override
    public List<ZWaveNode> getNodes() {
        return newArrayList(nodeMap.values());
    }

    @Override
    public ZWaveNode getNode(int nodeId) {
        return nodeMap.get(nodeId);
    }
}
