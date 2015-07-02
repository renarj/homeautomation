package com.oberasoftware.home.core.model.storage;

import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.jasdb.api.entitymapper.annotations.Id;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBEntity;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBProperty;

import java.util.Map;

/**
 * @author renarj
 */
@JasDBEntity(bagName = "devices")
public class DeviceItemImpl implements DeviceItem {
    private String id;
    private String pluginId;
    private String deviceId;
    private String name;
    private String controllerId;

    private Map<String, String> properties;

    public DeviceItemImpl(String id, String controllerId, String pluginId, String deviceId, String name, Map<String, String> properties) {
        this.controllerId = controllerId;
        this.pluginId = pluginId;
        this.id = id;
        this.name = name;
        this.deviceId = deviceId;
        this.properties = properties;
    }

    public DeviceItemImpl() {
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
    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    @Override
    @JasDBProperty
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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
    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }


    @Override
    public String toString() {
        return "DeviceItem{" +
                "id='" + id + '\'' +
                ", pluginId='" + pluginId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", name='" + name + '\'' +
                ", properties=" + properties +
                '}';
    }
}
