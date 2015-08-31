package com.oberasoftware.home.core.model.storage;

import com.oberasoftware.home.api.model.storage.Container;
import com.oberasoftware.jasdb.api.entitymapper.annotations.Id;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBEntity;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBProperty;

import java.util.Map;

/**
 * @author renarj
 */
@JasDBEntity(bagName = "containers")
public class ContainerImpl implements Container {

    private String id;
    private String name;
    private String dashboardId;
    private Map<String, String> properties;

    private String parentContainerId;

    public ContainerImpl(String id, String name, String dashboardId, String parentContainerId, Map<String, String> properties) {
        this.id = id;
        this.name = name;
        this.properties = properties;
        this.parentContainerId = parentContainerId;
        this.dashboardId = dashboardId;
    }

    public ContainerImpl() {
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
    public String getParentContainerId() {
        return parentContainerId;
    }

    public void setParentContainerId(String parentContainerId) {
        this.parentContainerId = parentContainerId;
    }

    @Override
    @JasDBProperty
    public String getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(String dashboardId) {
        this.dashboardId = dashboardId;
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
        return "ContainerImpl{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dashboardId='" + dashboardId + '\'' +
                ", properties=" + properties +
                ", parentContainerId='" + parentContainerId + '\'' +
                '}';
    }
}
