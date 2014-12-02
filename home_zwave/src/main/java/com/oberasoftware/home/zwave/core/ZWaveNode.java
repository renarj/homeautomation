package com.oberasoftware.home.zwave.core;

import com.oberasoftware.home.zwave.api.events.controller.NodeInformationEvent;
import org.apache.commons.lang.NotImplementedException;

/**
 * @author renarj
 */
public interface ZWaveNode {
    int getNodeId();

    default NodeStatus getNodeStatus() {
        return NodeStatus.INITIALIZING;
    }

    default NodeInformationEvent getNodeInformation() {
        throw new NotImplementedException("No node information is known");
    }

    default ZWaveNode setStatus(NodeStatus status) {
        throw new NotImplementedException("Can not set status on unidentified device status");
    }
}
