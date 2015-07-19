package com.oberasoftware.home.service.commands.handlers;

import com.oberasoftware.base.event.Event;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.commands.DeviceCommand;
import com.oberasoftware.home.api.commands.handlers.DeviceCommandHandler;
import com.oberasoftware.home.api.events.devices.DeviceCommandEvent;
import com.oberasoftware.home.api.extensions.AutomationExtension;
import com.oberasoftware.home.api.extensions.ExtensionManager;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.storage.HomeDAO;
import com.oberasoftware.home.core.model.storage.DeviceItemImpl;
import com.oberasoftware.home.core.model.storage.GroupItemImpl;
import com.oberasoftware.home.core.commands.GroupCommandImpl;
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
    public Event receive(DeviceCommandEvent event) {
        LOG.debug("Received a device command event: {}", event);

        DeviceCommand command = event.getCommand();
        LOG.debug("Looking up device details for command: {} and itemId: {}",command, command.getItemId());

        Optional<DeviceItemImpl> deviceData = homeDAO.findItem(DeviceItemImpl.class, command.getItemId());
        if(deviceData.isPresent()) {
            DeviceItem deviceItem = deviceData.get();
            String pluginId = deviceItem.getPluginId();

            AutomationExtension extension = extensionManager.getExtension(pluginId).get();
            DeviceCommandHandler commandHandler = (DeviceCommandHandler) extension.getCommandHandler();

            LOG.debug("Executing command: {} on extension: {}", command, extension);
            commandHandler.receive(deviceItem, command);
        } else {
            Optional<GroupItemImpl> groupItemImpl = homeDAO.findItem(GroupItemImpl.class, command.getItemId());
            if(groupItemImpl.isPresent()) {
                LOG.debug("Received a group command: {} for group: {}", event, groupItemImpl.get().getName());

                //return a new event, automation bus will process this
                return new GroupCommandImpl(command, groupItemImpl.get());
            } else {
                LOG.warn("Could not find deviceItem information for itemId: {}", command.getItemId());
            }
        }

        //no follow-up event to return
        return null;
    }
}
