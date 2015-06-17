package com.oberasoftware.home.api.storage.model;

/**
 * @author renarj
 */
public class VirtualItem implements Item {
    private final String id;
    private final String name;

    public VirtualItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
