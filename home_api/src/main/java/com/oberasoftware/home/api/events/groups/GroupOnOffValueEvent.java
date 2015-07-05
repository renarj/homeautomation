package com.oberasoftware.home.api.events.groups;

import com.oberasoftware.home.api.events.OnOffValue;
import com.oberasoftware.home.api.types.Value;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class GroupOnOffValueEvent implements GroupValueEvent {
    private final String groupId;
    private final List<String> itemIds;
    private final OnOffValue onOffValue;

    public GroupOnOffValueEvent(String groupId, List<String> itemIds, boolean on) {
        this.groupId = groupId;
        this.itemIds = itemIds;
        this.onOffValue = new OnOffValue(on);
    }

    public String getGroupId() {
        return groupId;
    }

    @Override
    public String getItemId() {
        return groupId;
    }

    @Override
    public List<String> getItemIds() {
        return itemIds;
    }

    @Override
    public String getLabel() {
        return OnOffValue.LABEL;
    }

    @Override
    public Value getValue() {
        return onOffValue;
    }
}
