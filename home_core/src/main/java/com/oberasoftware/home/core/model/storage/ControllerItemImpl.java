package com.oberasoftware.home.core.model.storage;

import com.oberasoftware.home.api.model.storage.ControllerItem;
import com.oberasoftware.jasdb.api.entitymapper.annotations.Id;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBEntity;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBProperty;

import java.util.Map;

/**
 * @author renarj
 */
@JasDBEntity(bagName = "controllers")
public class ControllerItemImpl implements ControllerItem {
    private String controllerId;
    private String id;
    private Map<String, String> properties;

    public ControllerItemImpl(String id, String controllerId, Map<String, String> properties) {
        this.id = id;
        this.controllerId = controllerId;
        this.properties = properties;
    }

    public ControllerItemImpl() {
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
    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "ControllerItem{" +
                ", controllerId='" + controllerId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
