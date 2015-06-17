package com.oberasoftware.home.hue;

import com.oberasoftware.home.api.managers.StateManager;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHLight;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
    private StateManager stateManager;

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void stop() {
        LOG.debug("Scheduling regular light state checks");
        scheduledExecutorService.scheduleAtFixedRate(this::checkAllLightStates, 0, MONITOR_INTERVAL, TimeUnit.MILLISECONDS);
    }

    public void checkAllLightStates() {
        LOG.debug("Checking hue light state");

        PHBridge bridge = hueConnector.getBridge();
        PHBridgeResourcesCache resourcesCache = bridge.getResourceCache();
        resourcesCache.getAllLights().forEach(this::checkLightState);
    }

    public void checkLightState(PHLight light) {
        

    }
}
