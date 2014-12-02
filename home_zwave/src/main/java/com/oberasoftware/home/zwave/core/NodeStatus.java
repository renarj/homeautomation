package com.oberasoftware.home.zwave.core;

/**
 * @author renarj
 */
public enum NodeStatus {
    INITIALIZING(0),
    IDENTIFIED(1),
    SLEEPING(2),
    AWAKE(2),
    ACTIVE(3),
    FULLY_OPERATIONAL(4);

    private final int sequence;

    NodeStatus(int sequence) {
        this.sequence = sequence;
    }

    public boolean hasMinimalStatus(NodeStatus status) {
        return this.sequence >= status.sequence;
    }
}
