package com.oberasoftware.home.api.events.devices;

import com.oberasoftware.home.api.events.DeviceEvent;
import com.oberasoftware.home.api.model.State;

/**
 * @author renarj
 */
public class StateUpdateEvent implements DeviceEvent {
    private final State state;

    public StateUpdateEvent(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}
