package com.oberasoftware.home.api.managers;

import com.oberasoftware.home.api.model.State;
import com.oberasoftware.home.api.model.Status;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.types.Value;

import java.util.Map;

/**
 * @author renarj
 */
public interface StateManager {
    State updateItemState(String itemId, String label, Value value);

    State updateDeviceState(DeviceItem item, String label, Value value);

    State updateStatus(DeviceItem item, Status newStatus);

    Map<String, State> getStates();

    State getState(String itemId);
}
