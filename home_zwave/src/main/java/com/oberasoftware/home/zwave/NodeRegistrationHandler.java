package com.oberasoftware.home.zwave;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.zwave.api.events.devices.NodeRegisteredEvent;
import com.oberasoftware.home.zwave.api.events.devices.NodeUpdatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author renarj
 */
@Component
public class NodeRegistrationHandler implements EventHandler {

    @Autowired
    private AutomationBus automationBus;

    @Autowired
    private DeviceRegistry deviceRegistry;

    @EventSubscribe
    public void receive(NodeRegisteredEvent event) throws Exception {
        deviceRegistry.updateNode(event.getNode());
    }

    @EventSubscribe
    public void receive(NodeUpdatedEvent event) {
        deviceRegistry.updateNode(event.getNode());

    }
}
