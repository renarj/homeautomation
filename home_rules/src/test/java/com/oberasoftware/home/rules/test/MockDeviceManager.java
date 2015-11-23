package com.oberasoftware.home.rules.test;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.api.managers.DeviceManager;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Renze de Vries
 */
@Component
public class MockDeviceManager implements DeviceManager {

    private List<DeviceItem> deviceItems = new CopyOnWriteArrayList<>();


    @Override
    public DeviceItem registerDevice(String pluginId, Device device) throws HomeAutomationException {
        return null;
    }

    public void addDevice(DeviceItem deviceItem) {
        this.deviceItems.add(deviceItem);
    }

    @Override
    public DeviceItem findDevice(String itemId) {
        return deviceItems.stream().filter(d -> d.getId().equals(itemId)).findFirst().get();
    }

    @Override
    public List<DeviceItem> getDevices(String controllerId) {
        return new ArrayList<>(deviceItems);
    }

    @Override
    public Optional<DeviceItem> findDeviceItem(String controllerId, String pluginId, String deviceId) {
        return deviceItems.stream().filter(d ->
                d.getControllerId().equals(controllerId) &&
                d.getPluginId().equals(pluginId) &&
                d.getDeviceId().equals(deviceId)).findFirst();
    }
}
