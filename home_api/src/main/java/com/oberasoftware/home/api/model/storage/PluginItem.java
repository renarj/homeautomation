package com.oberasoftware.home.api.model.storage;

/**
 * @author Renze de Vries
 */
public interface PluginItem extends Item {
    String getName();

    String getControllerId();

    String getPluginId();
}
