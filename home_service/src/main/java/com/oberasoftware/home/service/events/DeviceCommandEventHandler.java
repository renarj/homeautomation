package com.oberasoftware.home.service.events;

import com.oberasoftware.home.api.commands.DeviceCommand;
import com.oberasoftware.home.api.events.EventHandler;
import com.oberasoftware.home.api.events.EventSubscribe;
import com.oberasoftware.home.api.events.devices.DeviceCommandEvent;
import com.oberasoftware.home.api.extensions.AutomationExtension;
import com.oberasoftware.home.api.extensions.ExtensionManager;
import com.oberasoftware.home.api.storage.HomeDAO;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class DeviceCommandEventHandler implements EventHandler {
    private static final Logger LOG = getLogger(DeviceCommandEventHandler.class);

    @Autowired
    private ExtensionManager extensionManager;

    @Autowired
    private HomeDAO homeDAO;

    @EventSubscribe
    public void receive(DeviceCommandEvent event) {
        LOG.debug("Received a device command event: {}", event);

        DeviceCommand command = event.getCommand();
        LOG.debug("Looking up device details for command: {} and itemId: {}",command, command.getItemId());

        Optional<DeviceItem> deviceData = homeDAO.findItem(command.getItemId());
        if(deviceData.isPresent()) {
            DeviceItem deviceItem = deviceData.get();
            String pluginId = deviceItem.getPluginId();

            Optional<AutomationExtension> extension = extensionManager.getExtension(pluginId);

            LOG.debug("Executing command: {} on extension: {}", command, extension);
            extension.ifPresent(e -> e.getCommandHandler().receive(deviceItem, command));
        } else {
            LOG.warn("Could not find deviceItem information for itemId: {}", command.getItemId());
        }
    }
}
