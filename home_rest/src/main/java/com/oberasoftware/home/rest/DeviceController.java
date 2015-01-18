package com.oberasoftware.home.rest;

import com.oberasoftware.home.api.managers.DeviceManager;
import com.oberasoftware.home.api.managers.StateManager;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@RestController
@RequestMapping("/devices")
public class DeviceController {
    private static final Logger LOG = getLogger(DeviceController.class);

    @Autowired
    private DeviceManager deviceManager;

    @Autowired
    private StateManager stateManager;

    @RequestMapping
    public List<RestDevice> getDevices() {
        List<DeviceItem> deviceItems = deviceManager.getDevices();

        LOG.debug("Requested a list of all devices, found: {} devices", deviceItems.size());

        return deviceItems.stream()
                .map(d -> new RestDevice(d, stateManager.getState(d.getId())))
                .collect(Collectors.toList());
    }
}
