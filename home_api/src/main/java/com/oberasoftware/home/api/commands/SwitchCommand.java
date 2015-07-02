package com.oberasoftware.home.api.commands;

/**
 * @author renarj
 */
public interface SwitchCommand extends DeviceCommand {
    enum STATE {
        ON,
        OFF
    }

    STATE getState();
}
