package com.oberasoftware.home.example;

import com.oberasoftware.home.api.commands.Command;
import com.oberasoftware.home.api.commands.handlers.DeviceCommandHandler;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
public class ExampleCommandHandler implements DeviceCommandHandler {
    private static final Logger LOG = getLogger(ExampleCommandHandler.class);

    @Override
    public void receive(DeviceItem item, Command command) {
        LOG.debug("Received command: {} for device: {}", command, item);
    }
}
