package com.oberasoftware.home.api;

import com.oberasoftware.home.api.commands.Result;

/**
 * @author renarj
 */
public interface AutomationBus {
    Result publish(Message event);

    String getControllerId();
}
