package com.oberasoftware.home.mqtt;

/**
 * @author Renze de Vries
 */
public interface MQTTListener {
    void receive(String topic, String payload);
}
