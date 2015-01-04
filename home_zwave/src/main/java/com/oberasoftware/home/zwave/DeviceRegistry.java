package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.model.Device;

import java.util.List;

/**
 * @author renarj
 */
public interface DeviceRegistry {

    List<Device> getDevices();
}
