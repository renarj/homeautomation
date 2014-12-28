package com.oberasoftware.home.api.extensions;

import com.oberasoftware.home.api.model.Device;

import java.util.List;

/**
 * @author renarj
 */
public interface DeviceExtension extends AutomationExtension {
    List<Device> getDevices();
}
