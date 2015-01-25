package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.events.devices.DeviceNumericValueEvent;
import com.oberasoftware.home.api.events.devices.DeviceValueEvent;
import com.oberasoftware.home.api.events.devices.OnOffValueEvent;
import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.core.types.ValueImpl;
import com.oberasoftware.home.zwave.api.events.EventListener;
import com.oberasoftware.home.zwave.api.events.Subscribe;
import com.oberasoftware.home.zwave.api.events.devices.BatteryEvent;
import com.oberasoftware.home.zwave.api.events.devices.BinarySensorEvent;
import com.oberasoftware.home.zwave.api.events.devices.DeviceSensorEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class NodeValueEventListener implements EventListener<DeviceSensorEvent> {
    private static final Logger LOG = getLogger(NodeValueEventListener.class);

    @Autowired
    private AutomationBus automationBus;

    @Autowired
    private DeviceRegistry deviceRegistry;

    @Override
    public void receive(DeviceSensorEvent event) throws Exception {
        String label = event.getSensorType().getLabel();
        double value = event.getValue().doubleValue();
        String nodeId = Integer.toString(event.getNodeId());

        DeviceValueEvent valueEvent = new DeviceNumericValueEvent(getControllerId(), getZwaveId(), nodeId,
                new ValueImpl(VALUE_TYPE.DECIMAL, value), label);
        LOG.debug("Sending value event: {}", valueEvent);
        automationBus.publish(valueEvent);
    }

    @Subscribe
    public void receive(BatteryEvent event) throws Exception {
        String nodeId = Integer.toString(event.getNodeId());

        DeviceValueEvent valueEvent = new DeviceNumericValueEvent(getControllerId(), getZwaveId(),
                nodeId, new ValueImpl(VALUE_TYPE.NUMBER, event.getBatteryLevel()), "battery");
        LOG.debug("Received battery event: {}", valueEvent);

        automationBus.publish(valueEvent);
    }

    @Subscribe
    public void receive(BinarySensorEvent event) throws Exception {
        String nodeId = Integer.toString(event.getNodeId());

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
