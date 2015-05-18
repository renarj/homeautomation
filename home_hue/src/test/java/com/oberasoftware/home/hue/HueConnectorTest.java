package com.oberasoftware.home.hue;

import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.base.event.impl.LocalEventBus;
import com.oberasoftware.home.api.storage.model.PluginItem;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
public class HueConnectorTest {
    private static final Logger LOG = getLogger(HueConnectorTest.class);

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

        PluginItem plugin = new PluginItem("someId", "someController", "somePluginId", "Hue", properties);

        connector.connect(Optional.of(plugin));

        while(!connector.isConnected()) {
            sleepUninterruptibly(1, TimeUnit.SECONDS);
        }

        LOG.debug("We are connected, getting list of lights");
        PHBridge bridge = connector.getSdk().getSelectedBridge();


        Random rnd = new Random();

        bridge.getResourceCache().getAllLights().forEach(l -> {
            LOG.debug("Detecting a light: {}", l.getName());
            LOG.debug("Light id: {}", l.getIdentifier());
            LOG.debug("Light type: {}", l.getLightType());

            PHLightState st = new PHLightState();
            st.setEffectMode(PHLight.PHLightEffectMode.EFFECT_NONE);
            st.setBrightness(200);
            st.setTransitionTime(5);
            st.setHue(rnd.nextInt(65000));
            st.setSaturation(200);
            bridge.updateLightState(l, st);


        });

        Thread.sleep(100000);
    }
}
