package com.oberasoftware.home.experiment;

import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.zwave.api.events.devices.BinarySensorEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author renarj
 */
@Component
public class TriggerService implements EventListener<BinarySensorEvent> {

    private Map<Integer, Integer> sensorTriggerCount = new ConcurrentHashMap<>();

    @Override
    public void receive(BinarySensorEvent event) throws Exception {
        if(event.isTriggered()) {
            sensorTriggerCount.computeIfAbsent(event.getNodeId(), v -> 0);

            sensorTriggerCount.computeIfPresent(event.getNodeId(), (k, v) -> v + 1);
        }
    }

    public Map<Integer, Integer> getSensorTriggerCount() {
        return new HashMap<>(sensorTriggerCount);
    }

    public Integer getTriggerCount(int nodeId) {
        return sensorTriggerCount.getOrDefault(nodeId, 0);
    }
}
