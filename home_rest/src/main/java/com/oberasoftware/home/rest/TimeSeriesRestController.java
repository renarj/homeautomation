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
        if(timeSeriesStore != null) {
            try {
                return timeSeriesStore.findDataPoints(bus.getControllerId(), itemId, label, DEFAULT_TIME_SCALE, TimeUnit.HOURS);
            } catch(Exception e) {
                LOG.error("", e);
            }
        }
        return new ArrayList<>();
    }

}
