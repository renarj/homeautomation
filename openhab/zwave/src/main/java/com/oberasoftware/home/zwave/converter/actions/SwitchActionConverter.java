package com.oberasoftware.home.zwave.converter.actions;

import com.google.common.collect.Sets;
import com.oberasoftware.home.api.MessageConverter;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.ZWAVE_CONSTANTS;
import com.oberasoftware.home.zwave.api.actions.SwitchAction;
import com.oberasoftware.home.zwave.api.ZWaveAction;
import com.oberasoftware.home.zwave.converter.ZWaveConverter;
import com.oberasoftware.home.zwave.messages.ControllerMessageType;
import com.oberasoftware.home.zwave.messages.MessageType;
import com.oberasoftware.home.zwave.messages.ZWaveRawMessage;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author renarj
 */
public class SwitchActionConverter implements ZWaveConverter<SwitchAction, ZWaveRawMessage> {

    private static final int SWITCH_BINARY = 0x25;

    private static final AtomicInteger callbackId = new AtomicInteger(5);

    @Override
    public ZWaveRawMessage convert(SwitchAction switchAction) throws HomeAutomationException {
        int nodeId = switchAction.getDevice().getNodeId();
        int command = switchAction.getDesiredState() == SwitchAction.STATE.ON ? 0xFF : 0x00;

        ZWaveRawMessage message = new ZWaveRawMessage(nodeId, ControllerMessageType.SendData, MessageType.Request);
        byte[] newPayload = { 	(byte) nodeId,
                3, //message length
                (byte) SWITCH_BINARY,
                (byte) ZWAVE_CONSTANTS.SWITCH_BINARY_SET,
                (byte) command
        };

        message.setMessagePayload(newPayload);
        message.setCallbackId(callbackId.incrementAndGet());
        message.setTransmitOptions(0x01 | 0x04 | 0x20);

        return message;
    }

    @Override
    public Set<String> getSupportedTypeNames() {
        return Sets.newHashSet(SwitchAction.class.getSimpleName());
    }
}
