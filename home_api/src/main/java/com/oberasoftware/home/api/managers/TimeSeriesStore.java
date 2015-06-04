package com.oberasoftware.home.api.managers;

import com.oberasoftware.home.api.model.DataPoint;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author renarj
 */
public interface TimeSeriesStore extends StateStore {
    List<DataPoint> findDataPoints(String controllerId, String itemId, String label, long time, TimeUnit unit);
}
