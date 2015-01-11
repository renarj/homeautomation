package com.oberasoftware.home.api.extensions;

import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.storage.model.DevicePlugin;

import java.util.List;
import java.util.Optional;

/**
 * @author renarj
 */
public interface DeviceExtension extends AutomationExtension {

    boolean isDeviceReady();

    void activate(Optional<DevicePlugin> pluginItem);

    List<Device> getDevices();
}
