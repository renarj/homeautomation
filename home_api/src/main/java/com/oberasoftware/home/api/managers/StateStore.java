package com.oberasoftware.home.api.managers;

import com.oberasoftware.home.api.model.State;
import com.oberasoftware.home.api.types.Value;

import java.util.Map;

/**
 * @author renarj
 */
public interface StateStore {

    enum SUPPORTED_OPERATIONS {
        WRITE,
        READWRITE
    }

    void store(String itemId, String controllerId, String pluginId, String deviceId, String label, Value value);

    Map<String, State> getStates();

    State getState(String itemId);

    SUPPORTED_OPERATIONS getSupportedOperations();
}
