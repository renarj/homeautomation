package com.oberasoftware.home.service;

import com.oberasoftware.base.event.Event;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.impl.LocalEventBus;
import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.exceptions.RuntimeHomeAutomationException;
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
    public void publish(Event event) {
        eventBus.publish(event);
    }

    @Override
    public void registerHandler(EventHandler handler) {
        eventBus.registerHandler(handler);
    }
}
