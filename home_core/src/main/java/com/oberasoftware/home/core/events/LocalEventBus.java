package com.oberasoftware.home.core.events;

import com.google.common.reflect.TypeToken;
import com.oberasoftware.home.api.Message;
import com.oberasoftware.home.api.commands.Result;
import com.oberasoftware.home.api.events.EventBus;
import com.oberasoftware.home.api.events.EventHandler;
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
public class LocalEventBus implements EventBus {
    private static final Logger LOG = getLogger(LocalEventBus.class);

    private Map<Class<?>, List<HandlerEntry>> handlerEntries = new ConcurrentHashMap<>();

    @Autowired(required = false)
    private List<EventHandler> eventHandlers;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @PostConstruct
    public void loadListeners() {
        if(eventHandlers != null) {
            LOG.debug("Post processing all event listeners: {}", eventHandlers.size());
            for (EventHandler eventHandler : eventHandlers) {
                LOG.debug("Processing event listener: {}", eventHandler);
                processEventListener(eventHandler);
            }
        }
    }

    @Override
    public Result publish(Message event) {
        Future<?> f = executorService.submit(() -> {
            LOG.debug("Firing off an Async event: {}", event);
            notifyEventListeners(event);
        });


        return null;
    }

    @Override
    public void registerHandler(EventHandler handler) {
        LOG.debug("Registering handler: {}", handler);
        processEventListener(handler);
    }

    private void notifyEventListeners(Object event) {
        Set<String> handlersExecuted = new HashSet<>();
        TypeToken.of(event.getClass()).getTypes().forEach(o -> {
            List<HandlerEntry> handlers = handlerEntries.getOrDefault(o.getRawType(), new ArrayList<>());
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

            handlerEntries.computeIfAbsent(parameterType, v -> new ArrayList<>());
            handlerEntries.get(parameterType).add(new HandlerEntry(listenerInstance, method));
        }
    }
}
