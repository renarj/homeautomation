package com.oberasoftware.home.rest;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.events.devices.StateUpdateEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Controller
public class StateController implements EventHandler {
    private static final Logger LOG = getLogger(StateController.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @EventSubscribe
    public void receiveStateUpdate(StateUpdateEvent stateUpdateEvent) {
        LOG.debug("Received state: {}", stateUpdateEvent);
        messagingTemplate.convertAndSend("/topic/state", stateUpdateEvent.getState());
    }
}
