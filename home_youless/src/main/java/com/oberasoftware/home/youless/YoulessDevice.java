package com.oberasoftware.home.youless;

import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.model.Status;

import java.util.HashMap;
import java.util.Map;

/**
 * @author renarj
 */
public class YoulessDevice implements Device {

    private final String id;
    private final String name;

    public YoulessDevice(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Status getStatus() {
        return Status.ACTIVE;
    }

    @Override
    public Map<String, String> getProperties() {
        return new HashMap<>();
    }
}
