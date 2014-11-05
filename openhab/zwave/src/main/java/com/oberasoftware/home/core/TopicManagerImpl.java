package com.oberasoftware.home.core;

import com.oberasoftware.home.api.Topic;
import com.oberasoftware.home.api.TopicManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author renarj
 */
public class TopicManagerImpl implements TopicManager {

    private ConcurrentMap<String, Topic> messageQueueMap = new ConcurrentHashMap<>();

    @Override
    public <T> Topic<T> provideTopic(Class<T> messageType) {
        return getOrCreateQueue(messageType.getSimpleName());
    }

    @Override
    public <T> Topic<T> provideTopic(Class<T> messageType, String subType) {
        return getOrCreateQueue(messageType.getSimpleName() + subType);
    }

    private <T> Topic<T> getOrCreateQueue(String key) {
        return messageQueueMap.computeIfAbsent(key, v -> new FifoTopic<T>());
    }

    @Override
    public String toString() {
        return "TopicManagerImpl{" +
                "nrTopics=" + messageQueueMap.size() +
                '}';
    }
}
