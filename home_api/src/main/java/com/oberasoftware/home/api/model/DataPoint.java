package com.oberasoftware.home.api.model;

/**
 * @author renarj
 */
public interface DataPoint {
    double getTimestamp();

    String getItemId();

    String getLabel();

    double getValue();
}
