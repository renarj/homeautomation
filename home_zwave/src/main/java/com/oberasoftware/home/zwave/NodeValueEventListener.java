package com.oberasoftware.home.zwave;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.events.devices.DeviceNumericValueEvent;
import com.oberasoftware.home.api.events.devices.DeviceValueEvent;
import com.oberasoftware.home.api.events.devices.OnOffValueEvent;
import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.core.types.ValueImpl;
import com.oberasoftware.home.zwave.api.events.devices.BatteryEvent;
import com.oberasoftware.home.zwave.api.events.devices.BinarySensorEvent;
import com.oberasoftware.home.zwave.api.events.devices.DeviceEvent;
import com.oberasoftware.home.zwave.api.events.devices.DeviceSensorEvent;
import com.oberasoftware.home.zwave.api.events.devices.MeterEvent;
import com.oberasoftware.home.zwave.api.events.devices.SwitchEvent;
import com.oberasoftware.home.zwave.api.events.devices.SwitchLevelEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class NodeValueEventListener implements EventHandler {
    private static final Logger LOG = getLogger(NodeValueEventListener.class);

    @Autowired
    private AutomationBus automationBus;

    @Autowired
    private DeviceRegistry deviceRegistry;

    @EventSubscribe
    public void receive(DeviceSensorEvent event) throws Exception {
        String label = event.getSensorType().getLabel().toLowerCase();
        double value = event.getValue().doubleValue();
        String nodeId = getNodeId(event);

        DeviceValueEvent valueEvent = new DeviceNumericValueEvent(getControllerId(), getZwaveId(), nodeId,
                new ValueImpl(VALUE_TYPE.DECIMAL, value), label);
        LOG.debug("Sending value event: {}", valueEvent);
        automationBus.publish(valueEvent);
    }

    @EventSubscribe
    public void receive(SwitchEvent event) {
        String nodeId = getNodeId(event);
        LOG.debug("Received a switch event: {}", event);
        automationBus.publish(new OnOffValueEvent(getControllerId(), getZwaveId(), nodeId, event.isOn()));
    }

    @EventSubscribe
    public void receive(SwitchLevelEvent event) {
        String nodeId = getNodeId(event);
        LOG.debug("Received a switch level event: {}", event);
        automationBus.publish(new OnOffValueEvent(getControllerId(), getZwaveId(), nodeId, event.getValue() != 0));

        Value value = new ValueImpl(VALUE_TYPE.NUMBER, event.getValue());
        DeviceValueEvent valueEvent = new DeviceNumericValueEvent(getControllerId(), getZwaveId(), nodeId, value, "value");

        LOG.debug("Sending value event: {}", valueEvent);
        automationBus.publish(valueEvent);
    }

    @EventSubscribe
    public void receive(MeterEvent event) {
        String label = event.getScale().getLabel().toLowerCase();
        double value = event.getValue().doubleValue();
        String nodeId = getNodeId(event);
        DeviceValueEvent valueEvent = new DeviceNumericValueEvent(getControllerId(), getZwaveId(), nodeId,
                new ValueImpl(VALUE_TYPE.DECIMAL, value), label);
        LOG.debug("Sending value event: {}", valueEvent);
        automationBus.publish(valueEvent);

    }

    private String getNodeId(DeviceEvent event) {
        return event.getNodeId() + "-" + event.getEndpointId();
    }

    @EventSubscribe
    public void receive(BatteryEvent event) throws Exception {
        String nodeId = getNodeId(event);

        DeviceValueEvent valueEvent = new DeviceNumericValueEvent(getControllerId(), getZwaveId(),
                nodeId, new ValueImpl(VALUE_TYPE.NUMBER, event.getBatteryLevel()), "battery");
        LOG.debug("Received battery event: {}", valueEvent);

        automationBus.publish(valueEvent);
    }

    @EventSubscribe
    public void receive(BinarySensorEvent event) throws Exception {
        String nodeId = getNodeId(event);

        DeviceValueEvent valueEvent = new OnOffValueEvent(getControllerId(), getZwaveId(), nodeId, event.isTriggered());
        LOG.debug("Received a binary sensor event: {}", valueEvent);

        automationBus.publish(valueEvent);
    }

    private String getControllerId() {
        return automationBus.getControllerId();
    }

    private String getZwaveId() {
        return deviceRegistry.getZwaveId();
    }
}
