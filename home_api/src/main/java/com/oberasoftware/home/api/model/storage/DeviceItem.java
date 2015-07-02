package com.oberasoftware.home.api.model.storage;

import java.util.Map;

/**
 * @author Renze de Vries
 */
public interface DeviceItem extends Item {
    String getPluginId();

    String getDeviceId();

    String getName();

    String getControllerId();

    Map<String, String> getProperties();
}
