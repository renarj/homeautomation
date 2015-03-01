package com.oberasoftware.home.api.storage.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author renarj
 */
public class Container implements HomeEntity {

    private final String id;
    private final String name;
    private final List<UIItem> items = new ArrayList<>();

    private final String parentContainerId;

    public Container(String id, String name, String parentContainerId) {
        this.id = id;
        this.name = name;
        this.parentContainerId = parentContainerId;
    }

    public String getParentContainerId() {
        return parentContainerId;
    }

    public String getName() {
        return name;
    }

    public List<UIItem> getItems() {
        return items;
    }

    public void addItem(UIItem item) {
        this.items.add(item);
    }

    @Override
    public String getId() {
        return id;
    }
}
