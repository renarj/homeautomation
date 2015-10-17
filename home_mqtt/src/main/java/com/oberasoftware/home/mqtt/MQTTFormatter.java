package com.oberasoftware.home.mqtt;

/**
 * @author Renze de Vries
 */
public interface MQTTFormatter {
    void configure(String pattern);

    MQTTMessage format(String incomingTopic, String incomingPayload);
}
