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
public class TriggerCountRestService {

    @Autowired
    private TriggerService triggerService;

    @RequestMapping("/sensors")
    public List<SensorInformation> sensorData() {
        Map<Integer, Integer> sensorTriggerCount = triggerService.getSensorTriggerCount();

        List<SensorInformation> sensorInformations = new ArrayList<>();
        sensorTriggerCount.forEach((k, v) -> sensorInformations.add(new SensorInformation(k, v)));

        return sensorInformations;
    }

}
