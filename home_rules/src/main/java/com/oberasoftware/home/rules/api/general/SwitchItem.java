package com.oberasoftware.home.rules.api.general;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.oberasoftware.home.api.commands.SwitchCommand;
import com.oberasoftware.home.rules.api.ItemBlock;

/**
 * @author Renze de Vries
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
public class SwitchItem implements ItemBlock {

    private String itemId;
    private SwitchCommand.STATE state;

    public SwitchItem(String itemId, SwitchCommand.STATE state) {
        this.itemId = itemId;
        this.state = state;
    }

    public SwitchItem() {
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public SwitchCommand.STATE getState() {
        return state;
    }

    public void setState(SwitchCommand.STATE state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "SwitchAction{" +
                "itemId='" + itemId + '\'' +
                ", state=" + state +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SwitchItem that = (SwitchItem) o;

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
