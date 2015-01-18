package com.oberasoftware.home.api.model;

import com.oberasoftware.home.api.types.Value;

/**
 * @author renarj
 */
public interface StateItem {
    String getLabel();

    Value getValue();
}
