package com.oberasoftware.home.web.model;

import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.model.storage.GroupItem;
import com.oberasoftware.home.core.model.storage.GroupItemImpl;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class WebGroup extends GroupItemImpl {

    private final List<DeviceItem> devices;

    public WebGroup(GroupItem group, List<DeviceItem> devices) {
        super(group.getId(), group.getControllerId(), group.getName(), group.getDeviceIds(), group.getProperties());

        this.devices = devices;
    }

    public List<DeviceItem> getDevices() {
        return devices;
    }
}
