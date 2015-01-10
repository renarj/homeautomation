package com.oberasoftware.home.api.commands;

import java.util.Map;

/**
 * @author renarj
 */
public interface BasicCommand extends DeviceCommand {
    String getCommandType();

    Map<String, String> getProperties();
}
