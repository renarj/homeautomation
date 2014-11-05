package com.oberasoftware.home.core;

import com.oberasoftware.home.api.Topic;
import com.oberasoftware.home.api.EventListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author renarj
 */
public class FifoTopic<T> implements Topic<T> {

    private LinkedList<T> items = new LinkedList<>();

    private Lock lock = new ReentrantLock();

    private ConcurrentMap<String, List<EventListener<T>>> topicSubscribers = new ConcurrentHashMap<>();

    @Override
    public void push(T item) {
        lock.lock();
        try {
            items.addLast(item);
        } finally {
            lock.unlock();
        }

        notifyListeners(item);
    }

    @Override
    public Optional<T> peek() {
        lock.lock();
        try {
            return items.isEmpty() ? Optional.empty() : Optional.of(items.getFirst());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T pop() {
        lock.lock();
        try {
            if(!items.isEmpty()) {
                return items.removeFirst();
            } else {
                throw new NoSuchElementException("No element to pop");
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        return items.size();
    }

    private void notifyListeners(T item) {
        notifyMessageTypeListeners(item.getClass().getSimpleName(), item);
        notifyMessageTypeListeners("*", item);
    }

    private void notifyMessageTypeListeners(String messageType, T item) {
        List<EventListener<T>> topicListeners = topicSubscribers.getOrDefault(messageType, new ArrayList<>());
        topicListeners.forEach(l -> l.receive(item));
    }

    @Override
    public void subscribe(String messageType, EventListener<T> listener) {
        addSubscriber(messageType, listener);
    }

    @Override
    public void subscribe(EventListener<T> listener) {
        subscribe("*", listener);
    }

    private void addSubscriber(String name, EventListener<T> listener) {
        topicSubscribers.computeIfAbsent(name, v -> new ArrayList<>());
        topicSubscribers.get(name).add(listener);
    }

    @Override
    public String toString() {
        return "FifoTopic{" +
                "size=" + items.size() +
                ", listeners=" + topicSubscribers.size() +
                '}';
    }
}
