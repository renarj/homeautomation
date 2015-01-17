package com.oberasoftware.home.service;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.api.managers.ItemManager;
import com.oberasoftware.home.api.storage.CentralDatastore;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.api.storage.model.PluginItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class ItemManagerImpl implements ItemManager {
    private static final Logger LOG = getLogger(ItemManagerImpl.class);

    @Autowired
    private CentralDatastore centralDatastore;


    @Override
    public PluginItem createOrUpdatePlugin(String controllerId, String pluginId, String name, Map<String, String> properties) throws HomeAutomationException {
        centralDatastore.beginTransaction();
        try {
            Optional<PluginItem> optionalPlugin = centralDatastore.findPlugin(controllerId, pluginId);
            if (!optionalPlugin.isPresent()) {
                String itemId = generateId();
                LOG.debug("Plugin does not exist, storing in central datastore: {}", pluginId);

                return safelyStorePluginItem(itemId, controllerId, pluginId, name, properties);
            } else {
                PluginItem currentPlugin = optionalPlugin.get();
                if(havePropertiesChanged(currentPlugin.getProperties(), properties) && !name.equalsIgnoreCase(currentPlugin.getName())) {
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
        PluginItem item = createPluginItem(itemId, controllerId, pluginId, name, properties);

        LOG.debug("Storing plugin data: {}", item);
        return centralDatastore.store(item);
    }

    private PluginItem createPluginItem(String id, String controllerId, String pluginId, String name, Map<String, String> properties) {
        return new PluginItem(id, controllerId, pluginId, name, properties);
    }

    @Override
    public DeviceItem createOrUpdateDevice(String controllerId, String pluginId, String deviceId, String name, Map<String, String> properties) throws HomeAutomationException {
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
