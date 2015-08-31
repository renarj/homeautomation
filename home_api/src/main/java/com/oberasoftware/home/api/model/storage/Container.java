package com.oberasoftware.home.api.model.storage;

/**
 * @author Renze de Vries
 */
public interface Container extends MutableItem {
    String getParentContainerId();

    String getDashboardId();

    String getName();
}
