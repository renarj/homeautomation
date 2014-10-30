package com.oberasoftware.home;

import java.util.Optional;

/**
 * @author renarj
 */
public interface MessageTopic<T extends Message> {

    void push(T item);

    Optional<T> peek();

    T pop();

    int size();

    void subscribe(Class<? extends T> messageType, TopicListener<T> listener);

    void subscribe(TopicListener<T> listener);
}
