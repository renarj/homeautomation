package com.oberasoftware.home.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author renarj
 */
public class Floor extends Container {
    private final List<Room> rooms = new ArrayList<>();

    public Floor(String name) {
        super(name);
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
