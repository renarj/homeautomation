package com.oberasoftware.home.mqtt;

import com.oberasoftware.base.event.Event;

/**
 * @author Renze de Vries
 */
public class MQTTMessageImpl implements MQTTMessage {
    private final String contollerId;
    private final String itemId;
    private final String pluginId;
    private final Event event;

    public MQTTMessageImpl(String contollerId, String itemId, String pluginId, Event event) {
        this.contollerId = contollerId;
        this.itemId = itemId;
        this.pluginId = pluginId;
        this.event = event;
    }

    @Override
    public String getControllerId() {
        return contollerId;
    }

    @Override
    public String getPluginId() {
        return pluginId;
    }

    @Override
    public String getDeviceId() {
        return itemId;
    }

    @Override
    public Event getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "MQTTMessageImpl{" +
                "contollerId='" + contollerId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", pluginId='" + pluginId + '\'' +
                ", event=" + event +
                '}';
    }
}
