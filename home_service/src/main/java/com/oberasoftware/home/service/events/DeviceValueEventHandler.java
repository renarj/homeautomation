package com.oberasoftware.home.service.events;

import com.oberasoftware.home.api.events.EventHandler;
import com.oberasoftware.home.api.events.EventSubscribe;
import com.oberasoftware.home.api.events.devices.DeviceValueEvent;
import com.oberasoftware.home.api.managers.DeviceManager;
import com.oberasoftware.home.api.managers.StateManager;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class DeviceValueEventHandler implements EventHandler {
    private static final Logger LOG = getLogger(DeviceValueEventHandler.class);

    @Autowired
    private StateManager stateManager;

    @Autowired
    private DeviceManager deviceManager;

    @EventSubscribe
    public void receive(DeviceValueEvent event) {
        LOG.debug("Received a device value event: {}", event);

        Optional<DeviceItem> optionalItem = deviceManager.findDeviceItem(event.getControllerId(), event.getPluginId(), event.getDeviceId());
        if(optionalItem.isPresent()) {
            stateManager.updateState(optionalItem.get(), event.getLabel(), event.getValue());
        }
    }
}
