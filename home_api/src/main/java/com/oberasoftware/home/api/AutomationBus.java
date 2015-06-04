package com.oberasoftware.home.api;

import com.oberasoftware.base.event.Event;
import com.oberasoftware.base.event.EventHandler;

/**
 * @author renarj
 */
public interface AutomationBus {
    String getControllerId();

    void publish(Event event);

    void registerHandler(EventHandler handler);

}
