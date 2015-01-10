package com.oberasoftware.home.api.extensions;

/**
 * @author renarj
 */
public interface AutomationExtension {
    String getId();

    String getName();

    CommandHandler getCommandHandler();
}
