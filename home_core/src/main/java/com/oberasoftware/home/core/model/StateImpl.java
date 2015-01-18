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
        return stateItems.get(label);
    }

    @Override
    public Status getDeviceStatus() {
        return deviceStatus;
    }

    public void addStateItem(String label, StateItem stateItem) {
        this.stateItems.put(label, stateItem);
    }
}
