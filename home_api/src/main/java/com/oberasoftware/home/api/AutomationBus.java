package com.oberasoftware.home.api;

import com.oberasoftware.home.api.commands.Result;
import com.oberasoftware.home.api.events.Event;

/**
 * @author renarj
 */
public interface AutomationBus {
    Result publish(Event event);
}
