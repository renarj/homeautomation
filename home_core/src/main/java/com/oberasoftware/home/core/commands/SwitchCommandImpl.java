package com.oberasoftware.home.core.commands;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SwitchCommandImpl that = (SwitchCommandImpl) o;

        if (!itemId.equals(that.itemId)) return false;
        return state == that.state;

    }

    @Override
    public int hashCode() {
        int result = itemId.hashCode();
        result = 31 * result + state.hashCode();
        return result;
    }
}
