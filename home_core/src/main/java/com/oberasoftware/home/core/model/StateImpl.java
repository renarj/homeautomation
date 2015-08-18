package com.oberasoftware.home.core.model;

import com.google.common.collect.Lists;
import com.oberasoftware.home.api.model.State;
import com.oberasoftware.home.api.model.StateItem;
import com.oberasoftware.home.api.model.Status;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author renarj
 */
public class StateImpl implements State {

    private final String itemId;
    private final Status deviceStatus;
    private final ConcurrentMap<String, StateItem> stateItems = new ConcurrentHashMap<>();

    public StateImpl(String itemId, Status deviceStatus) {
        this.itemId = itemId;
        this.deviceStatus = deviceStatus;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public List<StateItem> getStateItems() {
        return Lists.newArrayList(stateItems.values());
    }

    @Override
    public StateItem getStateItem(String label) {
        return stateItems.get(label.toLowerCase());
    }

    @Override
    public Status getDeviceStatus() {
        return deviceStatus;
    }

    public boolean updateIfChanged(String label, StateItem stateItem) {
        String normalisedLabel = label.toLowerCase();

        boolean updated = true;
        if(stateItems.containsKey(normalisedLabel)) {
            //if the existing item is different than new item it is updated
            updated = !stateItems.get(normalisedLabel).equals(stateItem);
        }
        this.stateItems.put(normalisedLabel, stateItem);
        return updated;
    }

    @Override
    public String toString() {
        return "StateImpl{" +
                "itemId='" + itemId + '\'' +
                ", deviceStatus=" + deviceStatus +
                ", stateItems=" + stateItems +
                '}';
    }
}
