package com.oberasoftware.home.mqtt;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Renze de Vries
 */
public class MQTTBroker {
    private static final Logger LOG = LoggerFactory.getLogger(MQTTBroker.class);

    private final String host;
    private MqttClient client;
    private final AtomicBoolean connected = new AtomicBoolean(false);

    private List<MQTTListener> listeners = new CopyOnWriteArrayList<>();

    public MQTTBroker(String host) {
        this.host = host;
    }

    public synchronized void connect() throws HomeAutomationException {
        try {
            client = new MqttClient(host, "haas");
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    LOG.warn("Connect lost to host: {}", host);
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    listeners.forEach(l -> l.receive(s, new String(mqttMessage.getPayload())));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    LOG.debug("delivery complete");
                }
            });
            client.connect();
            connected.set(true);
        } catch (MqttException e) {
            throw new HomeAutomationException("Could not connect to MQTT broker: " + host);
        }
    }

    public void disconnect() {
        if(connected.get()) {
            try {
                client.disconnect();
            } catch (MqttException e) {
                LOG.error("Could not safely disconnect from MQTT broker: " + host);
            }
        }
    }

    public void subscribeTopic(String topic, MQTTListener listener) {
        if(connected.get()) {
            listeners.add(listener);

            try {
                client.subscribe(topic);
            } catch (MqttException e) {
                LOG.error("Could not subscribe to topic: " + topic, e);
            }
        }
    }
}
