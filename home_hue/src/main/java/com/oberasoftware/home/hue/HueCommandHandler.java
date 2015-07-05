package com.oberasoftware.home.hue;

import com.google.common.reflect.TypeToken;
import com.oberasoftware.home.api.commands.Command;
import com.oberasoftware.home.api.commands.handlers.GroupCommandHandler;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.model.storage.GroupItem;
import com.oberasoftware.home.hue.actions.HueCommandAction;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class HueCommandHandler implements GroupCommandHandler {
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
    public void receive(GroupItem groupItem, List<DeviceItem> items, Command command) {
        Optional<HueCommandAction<Command>> action = getAction(command);
        if(action.isPresent()) {
            LOG.debug("Executing group: {} action: {}", groupItem, command);

            action.get().receive(groupItem, items, command);
        }
    }

    @Override
    public void receive(DeviceItem item, Command command) {
        Optional<HueCommandAction<Command>> action = getAction(command);
        if(action.isPresent()) {
            LOG.debug("Executing device: {} action: {}", item, action.get());

            action.get().receive(item, command);
        }
    }

    private Optional<HueCommandAction<Command>> getAction(Command command) {
        LOG.debug("Received a command, finding action handler for Command: {}", command);

        final Set<String> actionsExecuted = new HashSet<>();
        for(TypeToken t : TypeToken.of(command.getClass()).getTypes()) {
            String typeName = t.getRawType().getName();
            LOG.debug("Checking an action for type: {}", typeName);

            if (!actionsExecuted.contains(typeName)) {
                actionsExecuted.add(typeName);

                Optional<HueCommandAction<Command>> action = ofNullable(actionMap.get(typeName));
                if (action.isPresent()) {
                    LOG.debug("Found handler: {} for command: {}", action.get(), command);

                    return action;
                } else {
                    LOG.debug("Unsupported command: {} for HUE plugin type: {}", command, typeName);
                }
            } else {
                LOG.debug("Type already executed, skipping: {}", typeName);
            }
        }

        return Optional.empty();
    }

}
