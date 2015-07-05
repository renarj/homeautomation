package com.oberasoftware.home.api.commands.handlers;

import com.oberasoftware.home.api.commands.Command;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.model.storage.GroupItem;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface GroupCommandHandler extends DeviceCommandHandler {
    void receive(GroupItem groupItem, List<DeviceItem> items, Command command);
}
