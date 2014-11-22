package com.oberasoftware.home.zwave.api;

import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.api.events.ZWaveEvent;

/**
 * @author renarj
 */
public interface Controller {
    void subscribe(EventListener<ZWaveEvent> topicListener);

    void send(ZWaveAction message) throws HomeAutomationException;
}
