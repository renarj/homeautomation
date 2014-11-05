package com.oberasoftware.home.zwave.converter.controller;

import com.oberasoftware.home.api.MessageConverter;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.api.events.ControllerEvent;
import com.oberasoftware.home.zwave.messages.ZWaveRawMessage;

/**
 * @author renarj
 */
public class ControllerEventConverter implements MessageConverter<ZWaveRawMessage, ControllerEvent> {
    @Override
    public ControllerEvent convert(ZWaveRawMessage source) throws HomeAutomationException {
        ControllerMessageType messageType = source.getMessageClass();

        return null;
    }

}
