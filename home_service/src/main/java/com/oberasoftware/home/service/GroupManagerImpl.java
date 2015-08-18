package com.oberasoftware.home.service;

import com.oberasoftware.home.api.managers.DeviceManager;
import com.oberasoftware.home.api.managers.GroupManager;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.model.storage.GroupItem;
import com.oberasoftware.home.core.model.storage.GroupItemImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Renze de Vries
 */
@Component
public class GroupManagerImpl extends GenericItemManagerImpl<GroupItem> implements GroupManager {

    @Autowired
    private DeviceManager deviceManager;

    @Override
    public List<DeviceItem> getDevices(String groupId) {
        List<String> devicesIds = getItem(groupId).getDeviceIds();
        return devicesIds.stream().map(deviceManager::findDevice).collect(Collectors.toList());
    }

    @Override
    protected Class<? extends GroupItem> getType() {
        return GroupItemImpl.class;
    }
}
