package com.oberasoftware.home.api.model.storage;

import java.util.Map;

/**
 * @author renarj
 */
public interface Item extends HomeEntity {
    Map<String, String> getProperties();
}
