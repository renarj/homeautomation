package com.oberasoftware.home.core;

import com.oberasoftware.home.api.managers.DeviceManager;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.api.storage.model.DevicePlugin;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author renarj
 */
@Component
public class DeviceManagerImpl implements DeviceManager {

    @Override
    public DeviceItem registerDevice(DevicePlugin plugin, Device device) {
        return null;
    }

    @Override
    public List<DeviceItem> getDevices() {
        return null;
    }
}
