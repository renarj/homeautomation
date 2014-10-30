package com.oberasoftware.home.impl;

import com.oberasoftware.home.Message;
import com.oberasoftware.home.MessageTopic;
import com.oberasoftware.home.TopicManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author renarj
 */
public class TopicManagerImpl implements TopicManager {

    private ConcurrentMap<String, MessageTopic> messageQueueMap = new ConcurrentHashMap<>();

    @Override
    public <T extends Message> MessageTopic<T> provideTopic(Class<T> messageType) {
        return getOrCreateQueue(messageType.getSimpleName());
    }

    @Override
    public <T extends Message> MessageTopic<T> provideTopic(Class<T> messageType, String subType) {
        return getOrCreateQueue(messageType.getSimpleName() + subType);
    }

    private <T extends Message> MessageTopic<T> getOrCreateQueue(String key) {
        return messageQueueMap.computeIfAbsent(key, v -> new FifoTopic<T>());
    }

    @Override
    public String toString() {
        return "TopicManagerImpl{" +
                "nrTopics=" + messageQueueMap.size() +
                '}';
    }
}
