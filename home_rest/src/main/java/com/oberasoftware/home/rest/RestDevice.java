package com.oberasoftware.home.rest;

import com.oberasoftware.home.api.model.State;
import com.oberasoftware.home.api.storage.model.DeviceItem;

/**
 * @author renarj
 */
public class RestDevice {
    private final DeviceItem item;
    private final State state;

    public RestDevice(DeviceItem item, State state) {
        this.item = item;
        this.state = state;
    }

    public DeviceItem getItem() {
        return item;
    }

    public State getState() {
        return state;
    }
}
