package com.oberasoftware.home.core.model.storage;

import com.oberasoftware.home.api.model.storage.GroupItem;
import com.oberasoftware.jasdb.api.entitymapper.annotations.Id;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBEntity;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBProperty;

import java.util.List;
import java.util.Map;

/**
 * @author renarj
 */
@JasDBEntity(bagName = "groups")
public class GroupItemImpl implements GroupItem {
    private String id;
    private String name;
    private String controllerId;

    private Map<String, String> properties;

    private List<String> deviceIds;

    public GroupItemImpl(String id, String controllerId, String name, List<String> deviceIds, Map<String, String> properties) {
        this.id = id;
        this.controllerId = controllerId;
        this.name = name;
        this.properties = properties;
        this.deviceIds = deviceIds;
    }

    public GroupItemImpl() {
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
    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "GroupItemImpl{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", controllerId='" + controllerId + '\'' +
                ", properties=" + properties +
                ", deviceIds=" + deviceIds +
                '}';
    }
}
