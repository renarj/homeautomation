package com.oberasoftware.home.api.commands;

import java.util.Map;

/**
 * @author renarj
 */
public interface BasicCommand extends ItemCommand {
    String getCommandType();

    Map<String, String> getProperties();
}
