package com.oberasoftware.home.api.model.storage;

import java.util.Map;

/**
 * @author Renze de Vries
 */
public interface VirtualItem extends Item {
    String getName();

    Map<String, String> getProperties();

    String getControllerId();
}
