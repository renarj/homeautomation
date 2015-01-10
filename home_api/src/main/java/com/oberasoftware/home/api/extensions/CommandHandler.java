package com.oberasoftware.home.api.extensions;

import com.oberasoftware.home.api.commands.Command;
import com.oberasoftware.home.api.commands.Result;
import com.oberasoftware.home.api.storage.model.DeviceItem;

/**
 * @author renarj
 */
public interface CommandHandler {
    Result receive(DeviceItem item, Command command);
}
