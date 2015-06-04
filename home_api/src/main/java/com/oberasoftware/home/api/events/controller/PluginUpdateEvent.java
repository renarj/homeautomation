package com.oberasoftware.home.api.events.controller;


import com.oberasoftware.base.event.Event;

import java.util.Map;

/**
 * @author renarj
 */
public class PluginUpdateEvent implements Event {
    private final String pluginId;
    private final String name;
    private final Map<String, String> properties;

    public PluginUpdateEvent(String pluginId, String name, Map<String, String> properties) {
        this.pluginId = pluginId;
        this.name = name;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public String getPluginId() {
        return pluginId;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "PluginUpdateEvent{" +
                "pluginId='" + pluginId + '\'' +
                ", properties='" + properties + '\'' +
                '}';
    }
}
