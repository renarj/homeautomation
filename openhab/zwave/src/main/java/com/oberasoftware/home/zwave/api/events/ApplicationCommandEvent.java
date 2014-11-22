package com.oberasoftware.home.zwave.api.events;

import com.oberasoftware.home.zwave.messages.CommandClass;

import static com.oberasoftware.home.zwave.messages.ZWaveRawMessage.bb2hex;

/**
 * @author renarj
 */
public class ApplicationCommandEvent implements ControllerEvent {

    private int nodeId;
    private int endpointId;
    private CommandClass commandClass;

    private byte[] payload;

    private byte[] original;

    public ApplicationCommandEvent(byte[] original, int nodeId, int endpointId, CommandClass commandClass, byte[] payload) {
        this.original = original;
        this.nodeId = nodeId;
        this.endpointId = endpointId;
        this.commandClass = commandClass;
        this.payload = payload;
    }

    @Override
    public boolean isTransactionCompleted() {
        return false;
    }

    public int getNodeId() {
        return nodeId;
    }

    public int getEndpointId() {
        return endpointId;
    }

    public CommandClass getCommandClass() {
        return commandClass;
    }

    public byte[] getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "ApplicationCommandEvent{" +
                "original=" + bb2hex(original) +
                "nodeId=" + nodeId +
                ", endpointId=" + endpointId +
                ", commandClass=" + commandClass.getLabel() +
                ", payload=" + bb2hex(payload) +
                '}';
    }
}
