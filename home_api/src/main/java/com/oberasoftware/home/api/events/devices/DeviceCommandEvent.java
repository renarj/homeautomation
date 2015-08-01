package com.oberasoftware.home.api.events.devices;

import com.oberasoftware.home.api.commands.DeviceCommand;
import com.oberasoftware.home.api.events.ItemEvent;

/**
 * @author renarj
 */
public class DeviceCommandEvent implements ItemEvent {

    private final String itemId;
    private final DeviceCommand command;

    public DeviceCommandEvent(String itemId, DeviceCommand command) {
        this.itemId = itemId;
        this.command = command;
    }

    public String getItemId() {
        return itemId;
    }

    public DeviceCommand getCommand() {
        return command;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceCommandEvent that = (DeviceCommandEvent) o;

        if (!itemId.equals(that.itemId)) return false;
        return command.equals(that.command);

    }

    @Override
    public int hashCode() {
        int result = itemId.hashCode();
        result = 31 * result + command.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DeviceCommandEvent{" +
                "itemId='" + itemId + '\'' +
                ", command=" + command +
                '}';
    }
}
