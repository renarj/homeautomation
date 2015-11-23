package com.oberasoftware.home.api.managers;

import com.oberasoftware.home.api.model.DataPoint;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

/**
 * @author renarj
 */
public interface TimeSeriesStore extends StateStore {

    enum GROUPING {
        MINUTE("60s"),
        HOUR("60m"),
        DAY("24h");

        private String timeString;

        GROUPING(String timeString) {
            this.timeString = timeString;
        }

        public String getTimeString() {
            return timeString;
        }

        public static GROUPING fromName(String name) {
            return asList(values()).stream().filter(g -> g.name().equalsIgnoreCase(name))
                    .findFirst().orElseGet(() -> MINUTE);
        }
    }

    List<DataPoint> findDataPoints(String controllerId, String itemId, String label, GROUPING grouping, long time, TimeUnit unit);
}
