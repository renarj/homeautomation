package com.oberasoftware.home.zwave.api;

import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;

/**
 * @author renarj
 */
public interface Controller {
    <T> void subscribe(EventListener<T> topicListener);

    void send(ZWaveAction message) throws HomeAutomationException;
}
