package com.oberasoftware.home.api.model.storage;

import java.util.Map;

/**
 * @author Renze de Vries
 */
public interface PluginItem extends Item {
    String getName();

    String getControllerId();

    String getPluginId();

    Map<String, String> getProperties();
}
