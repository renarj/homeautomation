package com.oberasoftware.home.core.model.storage;

import com.oberasoftware.home.api.model.storage.VirtualItem;
import com.oberasoftware.jasdb.api.entitymapper.annotations.Id;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBEntity;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBProperty;

import java.util.Map;

/**
 * @author Renze de Vries
 */
@JasDBEntity(bagName = "virtualitems")
public class VirtualItemImpl implements VirtualItem {
    private String id;
    private String name;
    private String controllerId;
    private Map<String, String> properties;

    public VirtualItemImpl(String id, String name, String controllerId, Map<String, String> properties) {
        this.id = id;
        this.name = name;
        this.controllerId = controllerId;
        this.properties = properties;
    }

    public VirtualItemImpl() {
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
        return "VirtualItemImpl{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", controllerId='" + controllerId + '\'' +
                ", properties=" + properties +
                '}';
    }
}
