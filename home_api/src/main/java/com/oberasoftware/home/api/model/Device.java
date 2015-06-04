package com.oberasoftware.home.api.model;

import java.util.Map;

/**
 * @author renarj
 */
public interface Device {
    String getId();

    String getName();

    Status getStatus();

    Map<String, String> getProperties();
}
