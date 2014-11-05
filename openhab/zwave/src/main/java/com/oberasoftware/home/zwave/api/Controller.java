package com.oberasoftware.home.zwave.api;

import com.oberasoftware.home.api.Topic;
import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.api.events.ZWaveEvent;
import com.oberasoftware.home.zwave.connector.ControllerConnector;

/**
 * @author renarj
 */
public interface Controller {
    void connect(ControllerConnector connector) throws HomeAutomationException;

    void disconnect() throws HomeAutomationException;

    Topic<ZWaveEvent> getEventQueue() throws HomeAutomationException;

    void subscribe(EventListener<ZWaveEvent> topicListener);

    void send(ZWaveAction message) throws HomeAutomationException;
}
