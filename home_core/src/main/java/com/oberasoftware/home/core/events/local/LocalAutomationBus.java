package com.oberasoftware.home.core.events.local;

import com.google.common.reflect.TypeToken;
import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.commands.Result;
import com.oberasoftware.home.api.events.Event;
import com.oberasoftware.home.api.events.EventSubscribe;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.Arrays.stream;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class LocalAutomationBus implements AutomationBus {
    private static final Logger LOG = getLogger(LocalAutomationBus.class);

    private Map<Class<?>, List<HandlerEntry>> eventHandlers = new ConcurrentHashMap<>();

    @Autowired(required = false)
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
    public Result publish(Event event) {
        Future<?> f = executorService.submit(() -> {
            LOG.debug("Firing off an Async event: {}", event);
            notifyEventListeners(event);
        });


        return null;
    }

    private void notifyEventListeners(Object event) {
        Set<String> handlersExecuted = new HashSet<>();
        TypeToken.of(event.getClass()).getTypes().forEach(o -> {
            List<HandlerEntry> handlers = eventHandlers.getOrDefault(o.getRawType(), new ArrayList<>());
            handlers.forEach(h -> {
                String handlerClass = h.getListenerInstance().getClass().getName();

                if (!handlersExecuted.contains(handlerClass)) {
                    handlersExecuted.add(handlerClass);
                    h.executeHandler(event);
                }
            });
        });
    }

    private void processEventListener(Object listenerInstance) {
        Class<?> eventListenerClass = listenerInstance.getClass();
        stream(eventListenerClass.getMethods())
                .filter(m -> m.getDeclaredAnnotation(EventSubscribe.class) != null)
                .filter(m -> !m.isBridge())
                .forEach(m -> addEventHandler(listenerInstance, m));
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
