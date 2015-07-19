package com.oberasoftware.home.rules.api;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.oberasoftware.home.api.commands.SwitchCommand;

/**
 * @author Renze de Vries
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
public class SwitchAction implements ItemAction {

    private String itemId;
    private SwitchCommand.STATE state;

    public SwitchAction(String itemId, SwitchCommand.STATE state) {
        this.itemId = itemId;
        this.state = state;
    }

    public SwitchAction() {
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
}
