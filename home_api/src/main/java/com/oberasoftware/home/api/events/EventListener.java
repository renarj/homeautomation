package com.oberasoftware.home.api.events;

/**
 * @author renarj
 */
public interface EventListener<T> {
    void receive(T event) throws Exception;
}
