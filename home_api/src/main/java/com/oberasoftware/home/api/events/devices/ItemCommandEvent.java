package com.oberasoftware.home.api.events.devices;

import com.oberasoftware.home.api.commands.ItemCommand;
import com.oberasoftware.home.api.events.ItemEvent;

/**
 * @author renarj
 */
public class ItemCommandEvent implements ItemEvent {

    private final String itemId;
    private final ItemCommand command;

    public ItemCommandEvent(String itemId, ItemCommand command) {
        this.itemId = itemId;
        this.command = command;
    }

    public String getItemId() {
        return itemId;
    }

    public ItemCommand getCommand() {
        return command;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemCommandEvent that = (ItemCommandEvent) o;

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
        return "ItemCommandEvent{" +
                "itemId='" + itemId + '\'' +
                ", command=" + command +
                '}';
    }
}
