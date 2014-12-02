package com.oberasoftware.home.zwave.core.impl;

import com.oberasoftware.home.zwave.api.events.controller.NodeInformationEvent;
import com.oberasoftware.home.zwave.core.NodeManager;
import com.oberasoftware.home.zwave.core.NodeStatus;
import com.oberasoftware.home.zwave.core.ZWaveNode;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.collect.Lists.newArrayList;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class NodeManagerImpl implements NodeManager {
    private static final Logger LOG = getLogger(NodeManagerImpl.class);

    private Map<Integer, ZWaveNode> nodeMap = new ConcurrentHashMap<>();

    @Override
    public void registerNode(int nodeId) {
        nodeMap.putIfAbsent(nodeId, new BasicNode(nodeId));
    }

    @Override
    public void registerNode(ZWaveNode node) {
        nodeMap.putIfAbsent(node.getNodeId(), node);
    }

    @Override
    public void markDead(int nodeId) {

    }

    @Override
    public boolean haveNodeMinimalStatus(NodeStatus nodeStatus) {
        return nodeMap.values().stream().allMatch(v -> v.getNodeStatus().hasMinimalStatus(nodeStatus));
    }

    @Override
    public NodeStatus getNodeStatus(int nodeId) {
        return getNode(nodeId).getNodeStatus();
    }

    @Override
    public ZWaveNode setNodeStatus(int nodeId, NodeStatus nodeStatus) {
        ZWaveNode node = getNode(nodeId);

        return replaceOrSetNode(node.setStatus(nodeStatus));
    }

    @Override
    public List<ZWaveNode> getNodes() {
        return newArrayList(nodeMap.values());
    }

    @Override
    public void setNodeInformation(int nodeId, NodeInformationEvent nodeInformationEvent) {
        replaceOrSetNode(new IdentifiedNode(nodeId, nodeInformationEvent));
    }

    private ZWaveNode replaceOrSetNode(ZWaveNode node) {
        int nodeId = node.getNodeId();
        nodeMap.put(nodeId, node);
        return node;
    }

    @Override
    public ZWaveNode getNode(int nodeId) {
        return nodeMap.get(nodeId);
    }
}
