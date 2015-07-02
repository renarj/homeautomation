package com.oberasoftware.home.example;

import com.oberasoftware.home.api.commands.Command;
import com.oberasoftware.home.api.commands.Result;
import com.oberasoftware.home.api.extensions.CommandHandler;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
public class ExampleCommandHandler implements CommandHandler {
    private static final Logger LOG = getLogger(ExampleCommandHandler.class);

    @Override
    public Result receive(DeviceItem item, Command command) {
        LOG.debug("Received command: {} for device: {}", command, item);
        return null;
    }
}
