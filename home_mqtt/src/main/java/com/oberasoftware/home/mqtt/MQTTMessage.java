package com.oberasoftware.home.mqtt;

import com.oberasoftware.base.event.Event;

/**
 * @author Renze de Vries
 */
public interface MQTTMessage {
    String getControllerId();

    String getPluginId();

    String getDeviceId();

    Event getEvent();
}
