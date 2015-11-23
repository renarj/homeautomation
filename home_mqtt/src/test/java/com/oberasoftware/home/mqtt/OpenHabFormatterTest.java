package com.oberasoftware.home.mqtt;

import com.oberasoftware.home.api.events.devices.DeviceValueEvent;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Renze de Vries
 */
public class OpenHabFormatterTest {
    @Test
    public void testParseTopic() {
        PathPatternFormatter formatter = new PathPatternFormatter();
        formatter.configure("/{controllerId}/out/{itemId}/{type}");

        MQTTMessage message = formatter.format("/openHAB/out/weatherState/state", "ON");
        assertThat(message.getControllerId(), is("openHAB"));
        assertThat(message.getDeviceId(), is("weatherState"));
        assertThat(message.getEvent(), notNullValue());

        DeviceValueEvent valueEvent = (DeviceValueEvent) message.getEvent();
        assertThat(valueEvent.getValue().asString(), is("ON"));
    }
}
