package com.oberasoftware.home.api.model.storage;

import java.util.Map;

/**
 * @author Renze de Vries
 */
public interface UIItem extends Item {
    long getWeight();

    String getContainerId();

    String getName();

    String getUiType();

    String getItemId();

    Map<String, String> getProperties();
}
