package com.oberasoftware.home.hue;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.events.OnOffValue;
import com.oberasoftware.home.api.events.devices.DeviceValueEvent;
import com.oberasoftware.home.api.events.devices.DeviceValueEventImpl;
import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.core.types.ValueImpl;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class HueLightMonitor {
    private static final Logger LOG = getLogger(HueLightMonitor.class);

    private static final int MONITOR_INTERVAL = 60000;

    @Autowired
    private HueConnector hueConnector;

    @Autowired
    private AutomationBus bus;

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void start() {
        LOG.debug("Scheduling regular light state checks");
        scheduledExecutorService.scheduleAtFixedRate(this::checkAllLightStates, 0, MONITOR_INTERVAL, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void stop() {
        scheduledExecutorService.shutdown();
    }

    public void checkAllLightStates() {
        if(hueConnector.isConnected()) {
            LOG.debug("Checking hue light state");
            PHBridge bridge = hueConnector.getBridge();

            PHBridgeResourcesCache resourcesCache = bridge.getResourceCache();
            resourcesCache.getAllLights().forEach(this::checkLightState);
        } else {
            LOG.debug("Skipping light state check, not connected to bridge");
        }
    }

    public void checkLightState(PHLight light) {
        LOG.debug("Checking light state: {}", light);

        String deviceId = light.getIdentifier();
        PHLightState lightState = light.getLastKnownLightState();

        Value onOffValue = new OnOffValue(lightState.isOn());

        bus.publish(new DeviceValueEventImpl(bus.getControllerId(),
                HueExtension.HUE_ID, deviceId, onOffValue, OnOffValue.LABEL));

        int brightness = lightState.getBrightness();
        int correctedScale = (int)((double)brightness/255 * 100);
        Value value = new ValueImpl(VALUE_TYPE.NUMBER, correctedScale);
        DeviceValueEvent valueEvent = new DeviceValueEventImpl(bus.getControllerId(),
                HueExtension.HUE_ID, deviceId, value, "value");
        bus.publish(valueEvent);
    }
}
