package com.oberasoftware.home.api.managers;

import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.storage.model.DeviceItem;

import java.util.List;
import java.util.Optional;

/**
 * @author renarj
 */
public interface DeviceManager {
    DeviceItem registerDevice(String pluginId, Device device) throws DataStoreException;

    List<DeviceItem> getDevices();

    Optional<DeviceItem> findDeviceItem(String controllerId, String pluginId, String deviceId);
}
