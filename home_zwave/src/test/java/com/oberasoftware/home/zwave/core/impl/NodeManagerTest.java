package com.oberasoftware.home.zwave.core.impl;

import com.oberasoftware.home.zwave.core.NodeManager;
import com.oberasoftware.home.zwave.core.NodeStatus;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author renarj
 */
public class NodeManagerTest {
    @Test
    public void testMinimalStatus() {
        NodeManager nodeManager = new NodeManagerImpl();
        nodeManager.registerNode(new BasicNode(1, NodeStatus.IDENTIFIED));
        nodeManager.registerNode(new BasicNode(2, NodeStatus.IDENTIFIED));
        nodeManager.registerNode(new BasicNode(3, NodeStatus.IDENTIFIED));

        assertThat(nodeManager.haveNodeMinimalStatus(NodeStatus.IDENTIFIED), is(true));

        nodeManager.registerNode(new BasicNode(4, NodeStatus.INITIALIZING));
        assertThat(nodeManager.haveNodeMinimalStatus(NodeStatus.IDENTIFIED), is(false));

        assertThat(nodeManager.haveNodeMinimalStatus(NodeStatus.AWAKE), is(false));
        assertThat(nodeManager.haveNodeMinimalStatus(NodeStatus.ACTIVE), is(false));
        assertThat(nodeManager.haveNodeMinimalStatus(NodeStatus.FULLY_OPERATIONAL), is(false));
    }
}
