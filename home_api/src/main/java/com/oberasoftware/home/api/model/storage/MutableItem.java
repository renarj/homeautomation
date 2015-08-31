package com.oberasoftware.home.api.model.storage;

import java.util.Map;

/**
 * @author Renze de Vries
 */
public interface MutableItem extends Item {
    void setProperties(Map<String, String> properties);

    void setId(String id);
}
