package com.oberasoftware.home.api;

/**
 * @author renarj
 */
public interface TopicManager {
    <T> Topic<T> provideTopic(Class<T> messageType);

    <T> Topic<T> provideTopic(Class<T> messageType, String subType);
}
