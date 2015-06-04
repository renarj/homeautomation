package com.oberasoftware.home.api.storage.model;

/**
 * @author renarj
 */
public class Container implements HomeEntity {

    private String id;
    private String name;

    private String parentContainerId;

    public Container(String id, String name, String parentContainerId) {
        this.id = id;
        this.name = name;
        this.parentContainerId = parentContainerId;
    }

    public Container() {
    }

    public String getParentContainerId() {
        return parentContainerId;
    }

    public void setParentContainerId(String parentContainerId) {
        this.parentContainerId = parentContainerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Container{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parentContainerId='" + parentContainerId + '\'' +
                '}';
    }
}
