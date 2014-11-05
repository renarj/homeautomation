package com.oberasoftware.home.zwave.converter.actions;

import com.google.common.collect.Sets;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.api.actions.controller.RequestNodeInfoAction;
import com.oberasoftware.home.zwave.api.ZWaveAction;
import com.oberasoftware.home.zwave.converter.ZWaveConverter;
import com.oberasoftware.home.zwave.messages.ZWaveRawMessage;

import java.util.Set;

/**
 * @author renarj
 */
public class RequestNodeInfoConverter implements ZWaveConverter<ZWaveAction, ZWaveRawMessage> {
    @Override
    public ZWaveRawMessage convert(ZWaveAction source) throws HomeAutomationException {
        RequestNodeInfoAction nodeInfoAction = (RequestNodeInfoAction) source;
        int nodeId = nodeInfoAction.getNodeId();

        ZWaveRawMessage message = new ZWaveRawMessage(nodeId,
                ZWaveRawMessage.SerialMessageClass.RequestNodeInfo, ZWaveRawMessage.SerialMessageType.Request,
                ZWaveRawMessage.SerialMessageClass.ApplicationUpdate);

        byte[] newPayload = { (byte) nodeId };
        message.setMessagePayload(newPayload);
        return message;
    }

    @Override
    public Set<String> getSupportedTypeNames() {
        return Sets.newHashSet(RequestNodeInfoAction.class.getSimpleName());
    }

    @Override
    public boolean isGenericConverter(ZWaveAction source) {
        return false;
    }

    @Override
    public boolean isSpecificConverter(ZWaveAction source) {
        return source instanceof RequestNodeInfoAction;
    }
}
