package com.oberasoftware.home.service.events.controller;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.events.EventHandler;
import com.oberasoftware.home.api.events.EventSubscribe;
import com.oberasoftware.home.api.events.controller.PluginUpdateEvent;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.api.managers.ItemManager;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class PluginUpdateHandler implements EventHandler {
    private static final Logger LOG = getLogger(PluginUpdateHandler.class);

    @Autowired
    private ItemManager itemManager;

    @Autowired
    private AutomationBus automationBus;

    @EventSubscribe
    public void receive(PluginUpdateEvent pluginUpdateEvent) throws HomeAutomationException {
        LOG.debug("Received a plugin update command: {}", pluginUpdateEvent);

        itemManager.createOrUpdatePlugin(automationBus.getControllerId(), pluginUpdateEvent.getPluginId(), pluginUpdateEvent.getName(), pluginUpdateEvent.getProperties());
    }
}
