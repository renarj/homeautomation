package com.oberasoftware.home.api.model.storage;

/**
 * @author Renze de Vries
 */
public interface Widget extends MutableItem {
    long getWeight();

    String getContainerId();

    String getName();

    String getWidgetType();

    String getItemId();
}
