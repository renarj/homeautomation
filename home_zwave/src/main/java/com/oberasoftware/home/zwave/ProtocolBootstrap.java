package com.oberasoftware.home.zwave;

import com.oberasoftware.home.zwave.api.actions.controller.ControllerCapabilitiesAction;
import com.oberasoftware.home.zwave.api.actions.controller.ControllerInitialDataAction;
import com.oberasoftware.home.zwave.api.actions.controller.GetControllerIdAction;
import com.oberasoftware.home.zwave.exceptions.HomeAutomationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class ProtocolBootstrap {
    private static final Logger LOG = getLogger(ProtocolBootstrap.class);

    @Autowired
    private ZWaveController zWaveController;

    public void startInitialization() {
        LOG.info("Initializing ZWave Network by doing network discovery");
        try {
            zWaveController.send(new ControllerCapabilitiesAction());
            zWaveController.send(new ControllerInitialDataAction());
            zWaveController.send(new GetControllerIdAction());
        } catch (HomeAutomationException e) {
            LOG.error("Cannot initialize ZWave network", e);
        }
    }
}
