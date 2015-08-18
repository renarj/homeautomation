package com.oberasoftware.home.service;

import com.google.common.collect.Maps;
import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.events.devices.StateUpdateEvent;
import com.oberasoftware.home.api.managers.StateManager;
import com.oberasoftware.home.api.managers.StateStore;
import com.oberasoftware.home.api.model.State;
import com.oberasoftware.home.api.model.Status;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.core.model.StateImpl;
import com.oberasoftware.home.core.model.StateItemImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class StateManagerImpl implements StateManager {
    private static final Logger LOG = getLogger(StateManagerImpl.class);

    private ConcurrentMap<String, StateImpl> itemStates = new ConcurrentHashMap<>();

    @Autowired(required = false)
    private List<StateStore> stateStores;

    @Autowired
    private AutomationBus automationBus;

    private boolean updateState(String itemId, String label, Value value) {
        LOG.debug("Updating state of item: {} with label: {} to value: {}", itemId, label, value);
        itemStates.putIfAbsent(itemId, new StateImpl(itemId, Status.UNKNOWN));
        StateImpl state = itemStates.get(itemId);

        return state.updateIfChanged(label, new StateItemImpl(label, value));
    }


    @Override
    public State updateItemState(String itemId, String label, Value value) {
        boolean updated = updateState(itemId, label, value);
        State state = itemStates.get(itemId);

        if(updated) {
            automationBus.publish(new StateUpdateEvent(state));
        }

        return itemStates.get(itemId);
    }

    @Override
    public State updateDeviceState(DeviceItem item, String label, Value value) {
        boolean updated = updateState(item.getId(), label, value);

        StateImpl state = itemStates.get(item.getId());
        if(updated) {
            updateStateStores(item, label, value);

            automationBus.publish(new StateUpdateEvent(state));
        }

        return state;
    }

    @Override
    public State updateStatus(DeviceItem item, Status newStatus) {
        String itemId = item.getId();

        if(itemStates.containsKey(itemId)) {
            State oldState = itemStates.get(itemId);

            StateImpl newState = new StateImpl(itemId, newStatus);
            oldState.getStateItems().forEach(si -> newState.updateIfChanged(si.getLabel(), si));

            itemStates.put(itemId, newState);
        } else {
            itemStates.put(itemId, new StateImpl(itemId, newStatus));
        }


        return null;
    }

    @Override
    public Map<String, State> getStates() {
        return Maps.newHashMap(itemStates);
    }

    @Override
    public State getState(String itemId) {
        return itemStates.get(itemId);
    }

    private void updateStateStores(DeviceItem item, String label, Value value) {
        if(stateStores != null) {
            stateStores.forEach(s -> s.store(item.getId(), item.getControllerId(), item.getPluginId(), item.getDeviceId(), label, value));
        }
    }
}
