package com.oberasoftware.home.hue.actions;

import com.oberasoftware.home.api.commands.Command;
import com.oberasoftware.home.api.model.storage.DeviceItem;

/**
 * @author renarj
 */
public interface HueCommandAction<T extends Command> {
    void receive(DeviceItem item, T command);
}
