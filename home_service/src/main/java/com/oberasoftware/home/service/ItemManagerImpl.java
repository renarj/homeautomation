package com.oberasoftware.home.service;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.api.managers.ItemManager;
import com.oberasoftware.home.core.model.storage.ControllerItemImpl;
import com.oberasoftware.home.core.model.storage.DeviceItemImpl;
import com.oberasoftware.home.core.model.storage.PluginItemImpl;
import com.oberasoftware.home.api.storage.CentralDatastore;
import com.oberasoftware.home.api.storage.HomeDAO;
import com.oberasoftware.home.api.model.storage.ControllerItem;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.model.storage.Item;
import com.oberasoftware.home.api.model.storage.PluginItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class ItemManagerImpl implements ItemManager {
    private static final Logger LOG = getLogger(ItemManagerImpl.class);

    @Autowired
    private CentralDatastore centralDatastore;

    @Autowired
    private HomeDAO homeDAO;

    @Override
    public ControllerItem createOrUpdateController(String controllerId) throws HomeAutomationException {
        centralDatastore.beginTransaction();
        try {
            Optional<ControllerItem> controllerItem = homeDAO.findController(controllerId);
            if (!controllerItem.isPresent()) {
                LOG.debug("Initial startup, new controller detected registering in central datastore");
                return centralDatastore.store(new ControllerItemImpl(generateId(), controllerId, new HashMap<>()));
            } else {
                LOG.debug("Controller: {} was already registered", controllerId);
                return controllerItem.get();
            }
        } finally {
            centralDatastore.commitTransaction();
        }
    }

    @Override
    public PluginItem createOrUpdatePlugin(String controllerId, String pluginId, String name, Map<String, String> properties) throws HomeAutomationException {
        centralDatastore.beginTransaction();
        try {
            Optional<PluginItem> optionalPlugin = homeDAO.findPlugin(controllerId, pluginId);
            if (!optionalPlugin.isPresent()) {
                String itemId = generateId();
                LOG.debug("Plugin does not exist, storing in central datastore: {}", pluginId);

                return safelyStorePluginItem(itemId, controllerId, pluginId, name, properties);
            } else {
                PluginItem currentPlugin = optionalPlugin.get();
                if(havePropertiesChanged(currentPlugin.getProperties(), properties) || !name.equals(currentPlugin.getName())) {
                    LOG.debug("Plugin information has changed, storing plugin item: {}", pluginId);

                    return safelyStorePluginItem(currentPlugin.getId(), controllerId, pluginId, name, properties);
                } else {
                    LOG.debug("Plugin information has not been updated, skipping storing");
                    return currentPlugin;
                }
            }
        } finally {
            centralDatastore.commitTransaction();
        }
    }

    private PluginItem safelyStorePluginItem(String itemId, String controllerId, String pluginId, String name, Map<String, String> properties) throws DataStoreException {
        PluginItemImpl item = createPluginItem(itemId, controllerId, pluginId, name, properties);

        LOG.debug("Storing plugin data: {}", item);
        return centralDatastore.store(item);
    }

    private PluginItemImpl createPluginItem(String id, String controllerId, String pluginId, String name, Map<String, String> properties) {
        return new PluginItemImpl(id, controllerId, pluginId, name, properties);
    }

    @Override
    public DeviceItem createOrUpdateDevice(String controllerId, String pluginId, String deviceId, String name, Map<String, String> properties) throws HomeAutomationException {
        centralDatastore.beginTransaction();
        try {
            Optional<DeviceItem> deviceItem = homeDAO.findDevice(controllerId, pluginId, deviceId);
            if(deviceItem.isPresent()) {
                DeviceItem item = deviceItem.get();

                if(havePropertiesChanged(item.getProperties(), properties) || !item.getName().equals(name)) {
                    LOG.debug("Device: {} already exist, properties have changed, updating device with id: {}", deviceId, item.getId());
                    return centralDatastore.store(new DeviceItemImpl(item.getId(), controllerId, pluginId, deviceId,
                            name, properties));
                } else {
                    LOG.debug("Device: {} has not changed, not updating item: {}", deviceId, item.getId());
                    return item;
                }
            } else {
                String id = generateId();
                LOG.debug("Device: {} does not yet exist, creating new with id: {}", deviceId, id);
                return centralDatastore.store(new DeviceItemImpl(id, controllerId, pluginId, deviceId,
                        name, properties));
            }
        } finally {
            centralDatastore.commitTransaction();
        }
    }

    @Override
    public List<ControllerItem> findControllers() {
        return homeDAO.findControllers();
    }

    @Override
    public List<PluginItem> findPlugins(String controllerId) {
        return homeDAO.findPlugins(controllerId);
    }

    @Override
    public List<DeviceItem> findDevices(String controllerId, String pluginId) {
        return homeDAO.findDevices(controllerId, pluginId);
    }

    @Override
    public List<DeviceItem> findDevices(String controllerId) {
        return homeDAO.findDevices(controllerId);
    }

    @Override
    public Item findItem(String id) {
        return null;
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    private boolean havePropertiesChanged(Map<String, String> previousProperties, Map<String, String> newProperties) {
        MapDifference<String, String> diff = Maps.difference(previousProperties, newProperties);

        return !diff.entriesOnlyOnRight().isEmpty();
    }
}
