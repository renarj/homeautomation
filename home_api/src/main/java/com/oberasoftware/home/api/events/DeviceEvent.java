package com.oberasoftware.home.api.events;

import com.oberasoftware.base.event.Event;

/**
 * @author renarj
 */
public interface DeviceEvent extends Event {
    String getControllerId();

    String getPluginId();

    String getDeviceId();
}
