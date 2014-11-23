package com.oberasoftware.home.zwave.api;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.messages.ZWaveRawMessage;

/**
 * @author renarj
 */
public interface ActionConverter {
    ZWaveRawMessage convert(ZWaveAction source) throws HomeAutomationException;
}
