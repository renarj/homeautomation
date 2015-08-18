package com.oberasoftware.home.service.events;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.events.devices.DeviceValueEvent;
import com.oberasoftware.home.api.events.items.ItemValueEvent;
import com.oberasoftware.home.api.managers.DeviceManager;
import com.oberasoftware.home.api.managers.StateManager;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.types.Value;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class ValueEventHandler implements EventHandler {
    private static final Logger LOG = getLogger(ValueEventHandler.class);

    @Autowired
    private StateManager stateManager;

    @Autowired
    private DeviceManager deviceManager;

    @EventSubscribe
    public void receive(DeviceValueEvent event) {
        LOG.debug("Received a device value event: {}", event);

        Optional<DeviceItem> optionalItem = deviceManager.findDeviceItem(event.getControllerId(), event.getPluginId(), event.getDeviceId());
        if (optionalItem.isPresent()) {
            LOG.debug("Updating state for device: {}", optionalItem);
            stateManager.updateDeviceState(optionalItem.get(), event.getLabel(), event.getValue());
        } else {
            LOG.warn("Received state for unknow device: {}", event);
        }
    }

    @EventSubscribe
    public void receive(ItemValueEvent event) {
        LOG.debug("Received an item value event: {}", event);
        String label = event.getLabel();
        Value value = event.getValue();

        LOG.debug("Updating state of group: {}", event.getItemId());
        stateManager.updateItemState(event.getItemId(), label, value);
    }
}
