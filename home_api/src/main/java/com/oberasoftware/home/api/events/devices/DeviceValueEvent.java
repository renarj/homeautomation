package com.oberasoftware.home.api.events.devices;

import com.oberasoftware.home.api.events.DeviceEvent;
import com.oberasoftware.home.api.types.Value;

/**
 * @author renarj
 */
public interface DeviceValueEvent extends DeviceEvent {

    String getControllerId();

    String getPluginId();

    String getDeviceId();

    String getLabel();

    Value getValue();
}
