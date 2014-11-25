package com.oberasoftware.home.zwave.handlers;

import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.api.events.Subscribe;
import com.oberasoftware.home.zwave.api.events.WaitForWakeUpEvent;
import com.oberasoftware.home.zwave.api.events.devices.WakeUpEvent;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class WakeUpHandler implements EventListener<WakeUpEvent> {
    private static final Logger LOG = getLogger(WakeUpHandler.class);

    @Override
    public void receive(WakeUpEvent event) throws Exception {

    }

    @Subscribe
    public void waitForWakeUp(WaitForWakeUpEvent waitForWakeUpEvent) {

    }
}
