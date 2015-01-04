package com.oberasoftware.home.api.storage.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author renarj
 */
public class Container implements HomeEntity {

    private final String name;

    private final List<Item> items = new ArrayList<>();
    private final List<ControllerItem> controllers = new ArrayList<>();

    public Container(String name) {
        this.name = name;
    }

    public List<ControllerItem> getControllers() {
        return controllers;
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    @Override
    public String getId() {
        return null;
    }
}
