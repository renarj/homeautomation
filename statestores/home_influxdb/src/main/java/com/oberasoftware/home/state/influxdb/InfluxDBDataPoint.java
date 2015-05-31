package com.oberasoftware.home.state.influxdb;

import com.oberasoftware.home.api.model.DataPoint;

import java.util.Map;

/**
 * @author renarj
 */
public class InfluxDBDataPoint implements DataPoint {
    private final String itemId;
    private final String label;
    private final double time;
    private final double value;


    public InfluxDBDataPoint(String itemId, Map<String, Object> pointData) {
        this.itemId = itemId;
        this.label = pointData.get("label").toString();

        this.time = (double)pointData.get("time");
        this.value = (double) pointData.get("mean");
    }

    @Override
    public double getTimestamp() {
        return time;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public double getValue() {
        return value;
    }
}
