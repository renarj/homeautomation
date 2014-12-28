package com.oberasoftware.home.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author renarj
 */
public class Location extends Container {
    private final List<Building> buildings = new ArrayList<>();

    public Location(String name) {
        super(name);
    }

    public List<Building> getBuildings() {
        return buildings;
    }
}
