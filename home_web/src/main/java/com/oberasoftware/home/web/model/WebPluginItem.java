package com.oberasoftware.home.web.model;

import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.model.storage.PluginItem;
import com.oberasoftware.home.core.model.storage.PluginItemImpl;

import java.util.List;

/**
 * @author renarj
 */
public class WebPluginItem extends PluginItemImpl {

    private final List<DeviceItem> devices;

    public WebPluginItem(PluginItem pluginItem, List<DeviceItem> devices) {
        super(pluginItem.getId(), pluginItem.getControllerId(), pluginItem.getPluginId(), pluginItem.getName(), pluginItem.getProperties());
        this.devices = devices;
    }

    public List<DeviceItem> getDevices() {
        return devices;
    }
}
