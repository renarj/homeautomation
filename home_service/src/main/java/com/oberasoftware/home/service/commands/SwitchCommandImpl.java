package com.oberasoftware.home.service.commands;

import com.oberasoftware.home.api.commands.SwitchCommand;

/**
 * @author renarj
 */
public class SwitchCommandImpl implements SwitchCommand {

    private final String itemId;
    private final STATE state;

    public SwitchCommandImpl(String itemId, STATE state) {
        this.itemId = itemId;
        this.state = state;
    }

    @Override
    public STATE getState() {
        return state;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public String toString() {
        return "SwitchCommandImpl{" +
                "state=" + state +
                ", itemId='" + itemId + '\'' +
                '}';
    }
}
