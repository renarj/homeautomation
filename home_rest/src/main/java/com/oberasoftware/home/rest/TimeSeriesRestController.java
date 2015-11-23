package com.oberasoftware.home.rest;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.managers.TimeSeriesStore;
import com.oberasoftware.home.api.model.DataPoint;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@RestController
@RequestMapping("/timeseries")
public class TimeSeriesRestController {
    private static final Logger LOG = getLogger(TimeSeriesRestController.class);

    private static final int DEFAULT_TIME_SCALE = 6;


    @Autowired(required = false)
    private TimeSeriesStore timeSeriesStore;

    @Autowired
    private AutomationBus bus;

    @RequestMapping("/item({itemId})/label({label})")
    public List<DataPoint> findDeviceData(@PathVariable String itemId,
                                          @PathVariable String label) {
        return findDeviceData(itemId, label, "minute", DEFAULT_TIME_SCALE);
    }


    @RequestMapping("/item({itemId})/label({label})/grouping({group})/hours({time})")
    public List<DataPoint> findDeviceData(@PathVariable String itemId,
                                          @PathVariable String label, @PathVariable String group, @PathVariable long time) {
        if(timeSeriesStore != null) {
            TimeSeriesStore.GROUPING grouping = TimeSeriesStore.GROUPING.fromName(group);
            LOG.debug("Doing item: {} time series request for: {} hours grouped by: {}", itemId, time, grouping);

            return timeSeriesStore.findDataPoints(bus.getControllerId(), itemId, label, grouping,
                    time, TimeUnit.HOURS);
        }
        return new ArrayList<>();
    }

}
