package com.oberasoftware.home.api.model.storage;

/**
 * @author Renze de Vries
 */
public interface Container extends HomeEntity {
    String getParentContainerId();

    String getDashboardId();

    String getName();
}
