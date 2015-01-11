package com.oberasoftware.home.api;

import com.oberasoftware.home.api.events.EventBus;

/**
 * @author renarj
 */
public interface AutomationBus extends EventBus {
    String getControllerId();
}
