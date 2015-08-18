package com.oberasoftware.home.rules.test;

import com.oberasoftware.home.api.managers.StateManager;
import com.oberasoftware.home.api.model.State;
import com.oberasoftware.home.api.model.Status;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.types.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Renze de Vries
 */
@Component
public class MockStateManager implements StateManager {

    private Map<String, State> stateMap = new HashMap<>();

    @Override
    public State updateItemState(String itemId, String label, Value value) {
        return null;
    }

    @Override
    public State updateDeviceState(DeviceItem item, String label, Value value) {
        return null;
    }

    @Override
    public State updateStatus(DeviceItem item, Status newStatus) {
        return null;
    }

    @Override
    public Map<String, State> getStates() {
        return null;
    }

    @Override
    public State getState(String itemId) {
        return stateMap.get(itemId);
    }

    public void addState(State state) {
        this.stateMap.put(state.getItemId(), state);
    }
}
