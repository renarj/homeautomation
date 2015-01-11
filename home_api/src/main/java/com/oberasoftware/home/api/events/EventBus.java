package com.oberasoftware.home.api.events;

import com.oberasoftware.home.api.Message;
import com.oberasoftware.home.api.commands.Result;

/**
 * @author renarj
 */
public interface EventBus {
    Result publish(Message event);

    void registerHandler(EventHandler handler);
}
