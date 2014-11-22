package com.oberasoftware.home.zwave.core;

import com.oberasoftware.home.api.exceptions.RuntimeEventException;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
public class HandlerEntry {
    private static final Logger LOG = getLogger(HandlerEntry.class);

    private Method eventMethod;
    private Object listenerInstance;

    public HandlerEntry(Object listenerInstance, Method eventMethod) {
        this.eventMethod = eventMethod;
        this.listenerInstance = listenerInstance;
    }

    public void executeHandler(Object event) {
        LOG.debug("Executing event listener: {} method: {} for event: {}", listenerInstance.getClass().getSimpleName(), eventMethod.getName(), event);
        try {
            eventMethod.invoke(listenerInstance, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeEventException("Unable to execute event listener", e);
        }
    }
}
