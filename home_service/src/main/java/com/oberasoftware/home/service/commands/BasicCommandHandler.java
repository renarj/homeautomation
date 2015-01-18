package com.oberasoftware.home.service.commands;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.commands.BasicCommand;
import com.oberasoftware.home.api.commands.DeviceCommand;
import com.oberasoftware.home.api.commands.converters.CommandConverter;
import com.oberasoftware.home.api.commands.converters.ConverterType;
import com.oberasoftware.home.api.events.devices.DeviceCommandEvent;
import com.oberasoftware.home.api.events.EventHandler;
import com.oberasoftware.home.api.events.EventSubscribe;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class BasicCommandHandler implements EventHandler {
    private static final Logger LOG = getLogger(BasicCommandHandler.class);


    @Autowired(required = false)
    private List<CommandConverter<BasicCommand, ? extends DeviceCommand>> commandConverters;

    @Autowired
    private AutomationBus automationBus;

    private Map<String, CommandConverter<BasicCommand, ? extends DeviceCommand>> commandConverterMap = new HashMap<>();

    @PostConstruct
    public void mapConverters() {
        commandConverters.forEach(c -> {
            Optional<Method> annotatedMethod = Arrays.stream(c.getClass().getMethods())
                    .filter(m -> m.getDeclaredAnnotation(ConverterType.class) != null)
                    .findFirst();

            if(annotatedMethod.isPresent()) {
                Method method = annotatedMethod.get();
                String commandType = method.getAnnotation(ConverterType.class).commandType();

                LOG.debug("Mapper found for commandType: {} on method: {}", commandType, method.getName());
                commandConverterMap.put(commandType, c);
            }
        });
    }

    @EventSubscribe
    public void receive(BasicCommand basicCommand) {
        LOG.debug("Received a basic command: {}", basicCommand);

        String commandType = basicCommand.getCommandType();

        if(commandConverterMap.containsKey(commandType)) {
            CommandConverter<BasicCommand, ? extends DeviceCommand> converter = commandConverterMap.get(commandType);

            DeviceCommand command = converter.map(basicCommand);
            LOG.debug("Converted: {} to command: {} sending to automation bus", basicCommand, command);

            automationBus.publish(new DeviceCommandEvent(command.getItemId(), command));
        } else {
            LOG.debug("No converter available for command type: {} on command: {}", commandType, basicCommand);
        }
    }
}
