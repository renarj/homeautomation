package com.oberasoftware.home;

/**
 * @author renarj
 */
public interface TopicListener<T extends Message> {
    void receive(T message);
}
