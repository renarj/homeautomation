package com.oberasoftware.home.zwave.converter.actions;

import com.google.common.collect.Sets;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.ZWAVE_CONSTANTS;
import com.oberasoftware.home.zwave.api.actions.SwitchAction;
import com.oberasoftware.home.zwave.converter.ZWaveConverter;
import com.oberasoftware.home.zwave.messages.types.ControllerMessageType;
import com.oberasoftware.home.zwave.messages.types.MessageType;
import com.oberasoftware.home.zwave.messages.ZWaveRawMessage;

import java.util.Set;

/**
 * @author renarj
 */
public class SwitchActionConverter implements ZWaveConverter<SwitchAction, ZWaveRawMessage> {

    private static final int SWITCH_BINARY = 0x25;

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

        message.setMessage(newPayload);

        return message;
    }

    @Override
    public Set<String> getSupportedTypeNames() {
        return Sets.newHashSet(SwitchAction.class.getSimpleName());
    }
}
