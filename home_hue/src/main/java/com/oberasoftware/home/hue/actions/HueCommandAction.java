package com.oberasoftware.home.hue.actions;

import com.oberasoftware.home.api.commands.Command;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.model.storage.GroupItem;

import java.util.List;

/**
 * @author renarj
 */
public interface HueCommandAction<T extends Command> {
    void receive(DeviceItem item, T command);

    void receive(GroupItem groupItem, List<DeviceItem> items, T command);
}
