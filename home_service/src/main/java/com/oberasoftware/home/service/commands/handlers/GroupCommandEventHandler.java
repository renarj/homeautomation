package com.oberasoftware.home.service.commands.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.commands.GroupCommand;
import com.oberasoftware.home.api.commands.handlers.CommandHandler;
import com.oberasoftware.home.api.commands.handlers.DeviceCommandHandler;
import com.oberasoftware.home.api.commands.handlers.GroupCommandHandler;
import com.oberasoftware.home.api.extensions.AutomationExtension;
import com.oberasoftware.home.api.extensions.ExtensionManager;
import com.oberasoftware.home.api.managers.DeviceManager;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.model.storage.GroupItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class GroupCommandEventHandler implements EventHandler {
    private static final Logger LOG = getLogger(GroupCommandEventHandler.class);

    @Autowired
    private ExtensionManager extensionManager;

    @Autowired
    private DeviceManager deviceManager;

    @EventSubscribe
    public void receive(GroupCommand groupCommand) {
        LOG.debug("Received a group command: {}", groupCommand);

        GroupItem groupItem = groupCommand.getGroup();
        List<String> deviceIds = groupItem.getDeviceIds();

        Map<String, List<DeviceItem>> pluginDevices = deviceIds.stream().map(deviceManager::findDevice)
                .collect(Collectors.groupingBy(DeviceItem::getPluginId));

        pluginDevices.forEach((k, v) -> {
            AutomationExtension extension = extensionManager.getExtension(k).get();

            CommandHandler commandHandler = extension.getCommandHandler();

            if(commandHandler instanceof GroupCommandHandler) {
                LOG.debug("CommandHandler: {} supports group commands, sending group command: {}", commandHandler, groupCommand);
                GroupCommandHandler groupCommandHandler = (GroupCommandHandler) commandHandler;

                groupCommandHandler.receive(groupItem, pluginDevices.get(k), groupCommand.getCommand());
            } else if(commandHandler instanceof DeviceCommandHandler) {
                LOG.debug("CommandHandler not able to support group command, sending individual commands: {}", groupCommand);
                DeviceCommandHandler deviceCommandHandler = (DeviceCommandHandler) commandHandler;

                pluginDevices.get(k).forEach(d -> deviceCommandHandler.receive(d, groupCommand.getCommand()));
            }
        });

    }
}
