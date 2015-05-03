package com.oberasoftware.home.service;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.Message;
import com.oberasoftware.home.api.commands.Result;
import com.oberasoftware.home.api.events.EventHandler;
import com.oberasoftware.home.api.exceptions.RuntimeHomeAutomationException;
import com.oberasoftware.home.core.events.LocalEventBus;
import nl.renarj.core.utilities.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author renarj
 */
@Component
public class LocalAutomationBus implements AutomationBus {
    @Autowired
    private LocalEventBus eventBus;

    @Value("${controller.id:}")
    private String controllerId;

    @Override
    public String getControllerId() {
        if(StringUtils.stringEmpty(controllerId)) {
            try {
                return InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                throw new RuntimeHomeAutomationException("Could not determine hostname, cannot start home automation system", e);
            }
        } else {
            return controllerId;
        }
    }

    @Override
    public Result publish(Message event) {
        return eventBus.publish(event);
    }

    @Override
    public void registerHandler(EventHandler handler) {
        eventBus.registerHandler(handler);
    }
}
