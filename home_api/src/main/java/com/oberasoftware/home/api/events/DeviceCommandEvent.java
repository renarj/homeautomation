package com.oberasoftware.home.api.events;

import com.oberasoftware.home.api.commands.DeviceCommand;

/**
 * @author renarj
 */
public class DeviceCommandEvent implements DeviceEvent {

    private final String itemId;
    private final DeviceCommand command;

    public DeviceCommandEvent(String itemId, DeviceCommand command) {
        this.itemId = itemId;
        this.command = command;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    public DeviceCommand getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "DeviceCommandEvent{" +
                "itemId='" + itemId + '\'' +
                ", command=" + command +
                '}';
    }
}
