package com.oberasoftware.home.web.model;

import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.api.storage.model.PluginItem;

import java.util.List;

/**
 * @author renarj
 */
public class WebPluginItem extends PluginItem {

    private final List<DeviceItem> devices;

    public WebPluginItem(PluginItem pluginItem, List<DeviceItem> devices) {
        super(pluginItem.getId(), pluginItem.getControllerId(), pluginItem.getPluginId(), pluginItem.getName(), pluginItem.getProperties());
        this.devices = devices;
    }

    public List<DeviceItem> getDevices() {
        return devices;
    }
}
