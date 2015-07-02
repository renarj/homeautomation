package com.oberasoftware.home.api.model.storage;

import java.util.List;
import java.util.Map;

/**
 * @author Renze de Vries
 */
public interface GroupItem extends Item {
    List<String> getDeviceIds();

    String getControllerId();

    String getName();

    Map<String, String> getProperties();
}
