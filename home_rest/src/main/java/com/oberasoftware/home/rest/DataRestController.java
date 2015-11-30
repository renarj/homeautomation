package com.oberasoftware.home.rest;

import com.oberasoftware.home.api.managers.ItemManager;
import com.oberasoftware.home.api.managers.StateManager;
import com.oberasoftware.home.api.model.State;
import com.oberasoftware.home.api.model.storage.ControllerItem;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.model.storage.Item;
import com.oberasoftware.home.api.model.storage.PluginItem;
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
@RequestMapping("/data")
public class DataRestController {
    private static final Logger LOG = getLogger(DataRestController.class);

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

    @RequestMapping("/controllers({controllerId})/devices")
    public List<RestItemDevice> getDevices(@PathVariable String controllerId) {
        LOG.debug("Requested list of all devices");

        List<DeviceItem> deviceItems = itemManager.findDevices(controllerId);
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
        return itemManager.findControllers();
    }

    @RequestMapping("/item({id})")
    public Item getItem(@PathVariable String id) {
        return itemManager.findItem(id);
    }

    @RequestMapping("/state({itemId})")
    public State getState(@PathVariable String itemId) {
        return stateManager.getState(itemId);
    }

}
