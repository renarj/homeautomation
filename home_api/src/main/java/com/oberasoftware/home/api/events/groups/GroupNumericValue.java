package com.oberasoftware.home.api.events.groups;

import com.oberasoftware.home.api.events.ValueEvent;
import com.oberasoftware.home.api.types.Value;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class GroupNumericValue implements GroupValueEvent, ValueEvent {
    private final String groupId;
    private final List<String> itemIds;
    private final Value value;
    private final String label;

    public GroupNumericValue(String groupId, List<String> itemIds, Value value, String label) {
        this.groupId = groupId;
        this.itemIds = itemIds;
        this.value = value;
        this.label = label;
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
    public Value getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
