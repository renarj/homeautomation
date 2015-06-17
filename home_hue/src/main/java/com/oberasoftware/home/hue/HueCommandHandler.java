package com.oberasoftware.home.hue;

import com.google.common.reflect.TypeToken;
import com.oberasoftware.home.api.commands.Command;
import com.oberasoftware.home.api.commands.Result;
import com.oberasoftware.home.api.extensions.CommandHandler;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.hue.actions.HueCommandAction;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class HueCommandHandler implements CommandHandler {
    private static final Logger LOG = getLogger(HueCommandHandler.class);

    @Autowired
    private List<HueCommandAction> actions;

    private ConcurrentMap<String, HueCommandAction<Command>> actionMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void prepareActions() {
        actions.forEach(this::processAction);
    }

    private void processAction(HueCommandAction<Command> actionInstance) {
        LOG.debug("Found action handler for HUE commands: {}", actionInstance);
        Class<?> actionClass = actionInstance.getClass();
        stream(actionClass.getMethods())
                .filter(m -> m.getName().equals("receive"))
                .filter(m -> !m.isBridge())
                .forEach(m -> addAction(actionInstance, m));
    }

    private void addAction(HueCommandAction<Command> actionInstance, Method method) {
        LOG.debug("Loading parameter type on method: {}", method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        if(parameterTypes.length == 2) {
            LOG.debug("Interested in message type: {}", parameterTypes[1].getName());
            Class<?> parameterType = parameterTypes[1];

            actionMap.put(parameterType.getName(), actionInstance);
        }
    }

    @Override
    public Result receive(DeviceItem item, Command command) {
        LOG.debug("Received a command for HUE device: {} command: {}", item.getDeviceId(), command);

        final Set<String> actionsExecuted = new HashSet<>();
        TypeToken.of(command.getClass()).getTypes().forEach(t -> {
            String typeName = t.getRawType().getName();
            LOG.debug("Checking an action for type: {}", typeName);

            if(!actionsExecuted.contains(typeName)) {
                actionsExecuted.add(typeName);

                Optional<HueCommandAction<Command>> action = ofNullable(actionMap.get(typeName));
                if (action.isPresent()) {
                    LOG.debug("Sending command: {} to HUE Action handler: {}", command, action.get());

                    action.get().receive(item, command);
                } else {
                    LOG.warn("Unsupported command: {} for HUE plugin type: {}", command, typeName);
                }
            } else {
                LOG.debug("Type already executed, skipping: {}", typeName);
            }
        });

        return null;
    }

}
