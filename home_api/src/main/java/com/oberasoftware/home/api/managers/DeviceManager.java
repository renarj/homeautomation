package com.oberasoftware.home.api.managers;

import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.api.storage.model.DevicePlugin;

import java.util.List;

/**
 * @author renarj
 */
public interface DeviceManager {
    DeviceItem registerDevice(DevicePlugin plugin, Device device);

    List<DeviceItem> getDevices();
}
