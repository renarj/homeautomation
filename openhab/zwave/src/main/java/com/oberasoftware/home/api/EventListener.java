package com.oberasoftware.home.api;

/**
 * @author renarj
 */
public interface EventListener<T> {
    void receive(T event);
}
