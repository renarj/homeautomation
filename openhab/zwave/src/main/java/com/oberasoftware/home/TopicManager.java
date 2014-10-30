package com.oberasoftware.home;

/**
 * @author renarj
 */
public interface TopicManager {
    <T extends Message> MessageTopic<T> provideTopic(Class<T> messageType);

    <T extends Message> MessageTopic<T> provideTopic(Class<T> messageType, String subType);
}
