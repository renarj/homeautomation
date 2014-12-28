package com.oberasoftware.home.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author renarj
 */
public abstract class Container {

    private final String name;
    private final List<Item> items = new ArrayList<>();

    private final List<HomeController> controllers = new ArrayList<>();

    public Container(String name) {
        this.name = name;
    }

    public List<HomeController> getControllers() {
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
}
