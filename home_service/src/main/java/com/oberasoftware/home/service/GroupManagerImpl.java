package com.oberasoftware.home.service;

import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.managers.DeviceManager;
import com.oberasoftware.home.api.managers.GroupManager;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.model.storage.GroupItem;
import com.oberasoftware.home.api.storage.CentralDatastore;
import com.oberasoftware.home.api.storage.HomeDAO;
import com.oberasoftware.home.core.model.storage.GroupItemImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class GroupManagerImpl implements GroupManager {
    private static final Logger LOG = getLogger(GroupManagerImpl.class);

    @Autowired
    private CentralDatastore centralDatastore;

    @Autowired
    private HomeDAO homeDAO;

    @Autowired
    private DeviceManager deviceManager;

    @Override
    public List<GroupItem> getGroups() {
        return homeDAO.findGroups();
    }

    @Override
    public List<GroupItem> getGroups(String controllerId) {
        return homeDAO.findGroups(controllerId);
    }

    @Override
    public List<DeviceItem> getDevices(String groupId) {
        List<String> devicesIds = getGroup(groupId).getDeviceIds();
        return devicesIds.stream().map(deviceManager::findDevice).collect(Collectors.toList());
    }

    @Override
    public GroupItem getGroup(String groupId) {
        return homeDAO.findItem(GroupItemImpl.class, groupId).get();
    }

    @Override
    public GroupItem store(GroupItem groupItem) {
        try {
            return centralDatastore.store(groupItem);
        } catch (DataStoreException e) {
            LOG.error("", e);
        }
        return null;
    }

    @Override
    public void delete(String groupId) {
        centralDatastore.beginTransaction();
        try {
            centralDatastore.delete(GroupItemImpl.class, groupId);
        } catch (DataStoreException e) {
            LOG.error("", e);
        } finally {
            centralDatastore.commitTransaction();
        }
    }
}
