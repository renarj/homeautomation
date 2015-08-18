package com.oberasoftware.home.rules.test;

import com.oberasoftware.base.event.Event;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.home.api.AutomationBus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Renze de Vries
 */
@Component
public class MockAutomationBus implements AutomationBus {

    private List<Event> publishedEvents = new CopyOnWriteArrayList<>();

    @Override
    public String getControllerId() {
        return "mockController";
    }

    @Override
    public void publish(Event event) {
        publishedEvents.add(event);
    }

    @Override
    public void registerHandler(EventHandler handler) {

    }

    public List<Event> getPublishedEvents() {
        return publishedEvents;
    }
}
