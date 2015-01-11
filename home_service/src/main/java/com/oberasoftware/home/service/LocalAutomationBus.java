package com.oberasoftware.home.service;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.Message;
import com.oberasoftware.home.api.commands.Result;
import com.oberasoftware.home.api.events.EventHandler;
import com.oberasoftware.home.api.exceptions.RuntimeHomeAutomationException;
import com.oberasoftware.home.core.events.LocalEventBus;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public String getControllerId() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeHomeAutomationException("Could not determine hostname, cannot start home automation system", e);
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
