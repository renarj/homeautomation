package com.oberasoftware.home.api.managers;

import com.oberasoftware.home.api.model.State;
import com.oberasoftware.home.api.model.Status;
import com.oberasoftware.home.api.types.Value;

import java.util.Map;

/**
 * @author renarj
 */
public interface StateManager {
    State updateState(String itemId, String label, Value value);

    State updateStatus(String itemId, Status newStatus);

    Map<String, State> getStates();

    State getState(String itemId);
}
