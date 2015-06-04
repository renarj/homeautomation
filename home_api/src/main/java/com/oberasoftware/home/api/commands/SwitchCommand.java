package com.oberasoftware.home.api.commands;

/**
 * @author renarj
 */
public interface SwitchCommand extends DeviceCommand {
    public enum STATE {
        ON,
        OFF
    }

    STATE getState();
}
