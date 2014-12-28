package com.oberasoftware.home.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author renarj
 */
public class Building extends Container {
    private final List<Floor> floors = new ArrayList<>();

    public Building(String name) {
        super(name);
    }

    @Override
    public List<Item> getItems() {
        return null;
    }
}
