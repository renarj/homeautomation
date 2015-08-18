package com.oberasoftware.home.api.managers;

import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.model.storage.GroupItem;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface GroupManager extends GenericItemManager<GroupItem> {

    List<DeviceItem> getDevices(String groupId);

}
