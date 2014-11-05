package com.oberasoftware.home.api;

import java.util.Optional;

/**
 * @author renarj
 */
public interface Topic<T> {

    void push(T item);

    Optional<T> peek();

    T pop();

    int size();

    void subscribe(String type, EventListener<T> listener);

    void subscribe(EventListener<T> listener);
}
