package com.oberasoftware.home.api.events.devices;

import com.oberasoftware.home.api.events.ItemEvent;
import com.oberasoftware.home.api.model.State;

/**
 * @author renarj
 */
public class StateUpdateEvent implements ItemEvent {
    private final State state;
    private final String itemId;

    public StateUpdateEvent(State state) {
        this.state = state;
        this.itemId = state.getItemId();
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    public State getState() {
        return state;
    }
}
