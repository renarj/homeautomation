package com.oberasoftware.home.api.events;

import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.api.exceptions.EventException;

/**
 * @author renarj
 */
public interface EventBus {
    void push(Object object) throws EventException;

    void pushAsync(Object object);

    void addListener(EventListener eventListener);
}
