package com.oberasoftware.home.api.storage.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author renarj
 */
public class ControllerItem implements Item {
    private final List<String> pluginIds = new ArrayList<>();

    private final String controllerId;
    private final String id;

    public ControllerItem(String id, String controllerId) {
        this.id = id;
        this.controllerId = controllerId;
    }

    public String getControllerId() {
        return controllerId;
    }

    public void addPluginId(String pluginId) {
        this.pluginIds.add(pluginId);
    }

    public List<String> getPluginIds() {
        return pluginIds;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ControllerItem{" +
                "pluginIds=" + pluginIds +
                ", controllerId='" + controllerId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
