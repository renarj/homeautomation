package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.events.controller.DeviceUpdateEvent;
import com.oberasoftware.home.zwave.api.events.EventListener;
import com.oberasoftware.home.zwave.api.events.Subscribe;
import com.oberasoftware.home.zwave.api.events.devices.NodeRegisteredEvent;
import com.oberasoftware.home.zwave.api.events.devices.NodeUpdatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author renarj
 */
@Component
public class NodeRegistrationHandler implements EventListener<NodeRegisteredEvent> {

    @Autowired
    private AutomationBus automationBus;

    @Autowired
    private DeviceRegistry deviceRegistry;

    @Override
    public void receive(NodeRegisteredEvent event) throws Exception {
        automationBus.publish(new DeviceUpdateEvent(deviceRegistry.getZwaveId(), new ZWaveDevice(event.getNode())));
    }

    @Subscribe
    public void receive(NodeUpdatedEvent event) {
        automationBus.publish(new DeviceUpdateEvent(deviceRegistry.getZwaveId(), new ZWaveDevice(event.getNode())));
    }
}
