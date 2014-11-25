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
    public void setNodeInformation(int nodeId, NodeInformationEvent nodeInformationEvent) {
        nodeMap.putIfAbsent(nodeId, new BasicNode(nodeId));
        if(nodeMap.get(nodeId) instanceof BasicNode) {
            LOG.debug("Upgrading node: {} to identified node with information: {}", nodeId, nodeInformationEvent);
            nodeMap.replace(nodeId, new IdentifiedNode(nodeId, nodeInformationEvent));
        }
    }

    @Override
    public ZWaveNode getNode(int nodeId) {
        return nodeMap.get(nodeId);
    }
}
