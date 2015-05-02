package com.oberasoftware.home.api.extensions;

import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.model.ExtensionResource;

import java.util.List;
import java.util.Optional;

/**
 * @author renarj
 */
public interface DeviceExtension extends AutomationExtension {

    List<Device> getDevices();

    default Optional<ExtensionResource> getDeviceImage(String deviceId) {
        return Optional.empty();
    }
}
