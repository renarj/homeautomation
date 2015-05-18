package com.oberasoftware.home.service.events.controller;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.events.controller.DeviceUpdateEvent;
import com.oberasoftware.home.api.managers.DeviceManager;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class DeviceUpdateHandler implements EventHandler {
    private static final Logger LOG = getLogger(DeviceUpdateHandler.class);

    @Autowired
    private DeviceManager deviceManager;

    @EventSubscribe
    public void receive(DeviceUpdateEvent event) throws Exception {
        LOG.debug("Received a device update for plugin: {} and device: {}", event.getPluginId(), event.getDevice());
        deviceManager.registerDevice(event.getPluginId(), event.getDevice());
    }
}
