package com.oberasoftware.home.experiment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author renarj
 */
@RestController
public class SensorDataService {

    @Autowired
    private SensorService sensorService;

    @RequestMapping("/sensors")
    public List<SensorInformation> sensorData() {
        Map<Integer, SensorInformation> sensorData = sensorService.getSensorInformation();

        List<SensorInformation> sensorInformations = new ArrayList<>();
        sensorData.forEach((k, v) -> sensorInformations.add(v));

        return sensorInformations;
    }

}
