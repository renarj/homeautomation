package com.oberasoftware.home.zwave.core.impl;

import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.api.events.EventBus;
import com.oberasoftware.home.api.events.Subscribe;
import com.oberasoftware.home.api.exceptions.EventException;
import com.oberasoftware.home.zwave.core.HandlerEntry;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Arrays.stream;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class EventBusImpl implements EventBus {
    private static final Logger LOG = getLogger(EventBusImpl.class);

    private Map<Class<?>, List<HandlerEntry>> eventHandlers = new ConcurrentHashMap<>();

    @Autowired
    private List<EventListener> eventListeners;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @PostConstruct
    public void loadListeners() {

        LOG.debug("Post processing all event listeners: {}", eventListeners.size());
        for(EventListener eventListener : eventListeners) {
            LOG.debug("Processing event listener: {}", eventListener);
            processEventListener(eventListener);
        }
    }

    @Override
    public void push(Object event) throws EventException {
        LOG.debug("Received an event: {}", event);

        notifyEventListeners(event);
    }

    @Override
    public void pushAsync(Object event) {
        executorService.submit(() -> {
            LOG.debug("Firing off an Async event: {}", event);
            notifyEventListeners(event);
        });
    }

    private void notifyEventListeners(Object event) {
        List<HandlerEntry> handlers = eventHandlers.getOrDefault(event.getClass(), new ArrayList<>());
        handlers.forEach(h -> h.executeHandler(event));
    }

    @Override
    public void addListener(EventListener eventListener) {
        processEventListener(eventListener);
    }

    private void processEventListener(Object listenerInstance) {
        Class<?> eventListenerClass = listenerInstance.getClass();
        stream(eventListenerClass.getMethods())
                .filter(m -> m.getName().equals("receive") || m.getDeclaredAnnotation(Subscribe.class) != null)
                .forEach(m -> addEventHandler(listenerInstance, m));

//        for(Method method : eventListenerClass.getMethods()) {
//            if(method.getName().equals("receive") && !method.isBridge()) {
//                addEventHandler(method);
//            } else if(method.getDeclaredAnnotation(Subscribe.class) != null) {
//                addEventHandler(method);
//            }
//        }
    }

    private void addEventHandler(Object listenerInstance, Method method) {
        LOG.debug("Loading parameter type on method: {}", method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        if(parameterTypes.length == 1) {
            LOG.debug("Interested in message type: {}", parameterTypes[0].getSimpleName());
            Class<?> parameterType = parameterTypes[0];

            eventHandlers.computeIfAbsent(parameterType, v -> new ArrayList<>());
            eventHandlers.get(parameterType).add(new HandlerEntry(listenerInstance, method));
        }
    }
}
