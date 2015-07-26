package com.oberasoftware.home.example;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.commands.Command;
import com.oberasoftware.home.api.commands.SwitchCommand;
import com.oberasoftware.home.api.commands.handlers.DeviceCommandHandler;
import com.oberasoftware.home.api.events.devices.OnOffValueEvent;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class ExampleCommandHandler implements DeviceCommandHandler {
    private static final Logger LOG = getLogger(ExampleCommandHandler.class);

    @Autowired
    private AutomationBus automationBus;

    @Override
    public void receive(DeviceItem item, Command command) {
        LOG.debug("Received command: {} for device: {}", command, item);

        if(command instanceof SwitchCommand) {
            SwitchCommand switchCommand = (SwitchCommand) command;

            automationBus.publish(new OnOffValueEvent(automationBus.getControllerId(), "example", item.getDeviceId(),
                    switchCommand.getState() == SwitchCommand.STATE.ON));
        }

    }
}
