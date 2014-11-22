package com.oberasoftware.home.zwave.converter.actions.controller;

import com.google.common.collect.Sets;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.api.actions.controller.ControllerCapabilitiesAction;
import com.oberasoftware.home.zwave.converter.ZWaveConverter;
import com.oberasoftware.home.zwave.messages.ControllerMessageType;
import com.oberasoftware.home.zwave.messages.MessageType;
import com.oberasoftware.home.zwave.messages.ZWaveRawMessage;

import java.util.Set;

/**
 * @author renarj
 */
public class ControllerCapabilitiesActionConverter implements ZWaveConverter<ControllerCapabilitiesAction, ZWaveRawMessage> {
    @Override
    public Set<String> getSupportedTypeNames() {
        return Sets.newHashSet(ControllerCapabilitiesAction.class.getSimpleName());
    }

    @Override
    public ZWaveRawMessage convert(ControllerCapabilitiesAction source) throws HomeAutomationException {
        return new ZWaveRawMessage(ControllerMessageType.GetCapabilities, MessageType.Request);
    }
}
