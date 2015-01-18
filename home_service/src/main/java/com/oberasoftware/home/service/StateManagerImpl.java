package com.oberasoftware.home.service;

import com.google.common.collect.Maps;
import com.oberasoftware.home.api.managers.StateManager;
import com.oberasoftware.home.api.model.State;
import com.oberasoftware.home.api.model.Status;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.core.model.StateImpl;
import com.oberasoftware.home.core.model.StateItemImpl;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

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

    @Override
    public State updateState(String itemId, String label, Value value) {
        LOG.debug("Updating state of item: {} with label: {} to value: {}", itemId, label, value);
        itemStates.putIfAbsent(itemId, new StateImpl(itemId, Status.UNKNOWN));
        StateImpl state = itemStates.get(itemId);
        state.addStateItem(label, new StateItemImpl(label, value));

        return state;
    }

    @Override
    public State updateStatus(String itemId, Status newStatus) {
        if(itemStates.containsKey(itemId)) {
            State oldState = itemStates.get(itemId);

            StateImpl newState = new StateImpl(itemId, newStatus);
            oldState.getStateItems().forEach(si -> newState.addStateItem(si.getLabel(), si));

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
}
