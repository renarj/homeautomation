package com.oberasoftware.home.hue;

import com.oberasoftware.home.api.events.EventBus;
import com.oberasoftware.home.api.storage.model.DevicePlugin;
import com.oberasoftware.home.core.events.LocalEventBus;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author renarj
 */
public class HueConnectorTest {
    @Test
    public void testConnect() throws InterruptedException {
        EventBus bus = new LocalEventBus();

        HueConnectorImpl connector = new HueConnectorImpl();
        connector.setAutomationBus(bus);
        bus.registerHandler(connector);

        connector.connect(Optional.empty());

        Thread.sleep(100000);
    }

    @Test
    public void testConnectExistingUser() throws InterruptedException {
        EventBus bus = new LocalEventBus();

        HueConnectorImpl connector = new HueConnectorImpl();
        connector.setAutomationBus(bus);
        bus.registerHandler(connector);

        Map<String, String> properties = new HashMap<>();
        properties.put("bridgeIp", "10.1.0.249");
        properties.put("username", "e13002c8-5286-4f28-a6c1-0307afa79445");

        DevicePlugin plugin = new DevicePlugin("someId", "someController", "somePluginId", "Hue", properties);

        connector.connect(Optional.of(plugin));

        Thread.sleep(100000);
    }
}
