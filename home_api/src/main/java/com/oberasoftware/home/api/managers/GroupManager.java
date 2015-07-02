package com.oberasoftware.home.api.managers;

import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.model.storage.GroupItem;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface GroupManager {
    List<GroupItem> getGroups();

    List<GroupItem> getGroups(String controllerId);

    List<DeviceItem> getDevices(String groupId);

    GroupItem getGroup(String groupId);

    GroupItem store(GroupItem groupItem);

    void delete(String groupId);
}
