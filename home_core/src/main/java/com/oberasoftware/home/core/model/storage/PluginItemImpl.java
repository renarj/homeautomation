package com.oberasoftware.home.core.model.storage;

import com.oberasoftware.home.api.model.storage.PluginItem;
import com.oberasoftware.jasdb.api.entitymapper.annotations.Id;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBEntity;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBProperty;

import java.util.Map;

/**
 * @author renarj
 */
@JasDBEntity(bagName = "plugins")
public class PluginItemImpl implements PluginItem {
    private String controllerId;
    private String pluginId;

    private String id;
    private String name;

    private Map<String, String> properties;

    public PluginItemImpl(String id, String controllerId, String pluginId, String name, Map<String, String> properties) {
        this.controllerId = controllerId;
        this.pluginId = pluginId;
        this.id = id;
        this.name = name;
        this.properties = properties;
    }

    public PluginItemImpl() {
    }

    @Override
    @Id
    @JasDBProperty
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    @JasDBProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    @JasDBProperty
    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    @Override
    @JasDBProperty
    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    @Override
    @JasDBProperty
    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "DevicePlugin{" +
                ", controllerId='" + controllerId + '\'' +
                ", pluginId='" + pluginId + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
