package com.oberasoftware.home.rest;

import com.oberasoftware.home.api.managers.DeviceManager;
import com.oberasoftware.home.api.managers.ItemManager;
import com.oberasoftware.home.api.managers.StateManager;
import com.oberasoftware.home.api.storage.model.ControllerItem;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.api.storage.model.Item;
import com.oberasoftware.home.api.storage.model.PluginItem;
import com.oberasoftware.home.rest.model.RestItemDevice;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@RestController
@RequestMapping("/")
public class HomeController {
    private static final Logger LOG = getLogger(HomeController.class);

    @Autowired
    private DeviceManager deviceManager;

    @Autowired
    private ItemManager itemManager;

    @Autowired
    private StateManager stateManager;

    @RequestMapping("/controllers({controllerId})/plugins({pluginId})/devices")
    public List<RestItemDevice> getDevices(@PathVariable String controllerId, @PathVariable String pluginId) {
        LOG.debug("Requested a list of all devices for controller: {} and plugin: {}", controllerId, pluginId);

        List<DeviceItem> deviceItems = itemManager.findDevices(controllerId, pluginId);

        return deviceItems.stream()
                .map(d -> new RestItemDevice(d, stateManager.getState(d.getId())))
                .collect(Collectors.toList());
    }

    @RequestMapping("/controllers({controllerId})/plugins")
    public List<PluginItem> getPlugins(@PathVariable String controllerId) {
        LOG.debug("Requested a list of all plugins for controller: {}", controllerId);

        return itemManager.findPlugins(controllerId);
    }

    @RequestMapping("/controllers")
    public List<ControllerItem> getControllers() {
        LOG.debug("Requested a list of all controllers");
        return itemManager.findControllers();//.stream().map(RestHomeController::new).collect(Collectors.toList());
    }

    @RequestMapping("/item({id}")
    public Item getItem(@PathVariable String id) {
        return itemManager.findItem(id);
    }

    @RequestMapping("/devices")
    public List<RestItemDevice> getDevices() {
        LOG.debug("Requested list of all devices");

        List<DeviceItem> deviceItems = itemManager.findDevices();
        return deviceItems.stream()
                .map(d -> new RestItemDevice(d, stateManager.getState(d.getId())))
                .collect(Collectors.toList());
    }
}
