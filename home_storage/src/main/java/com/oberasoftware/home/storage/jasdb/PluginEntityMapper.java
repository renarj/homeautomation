package com.oberasoftware.home.storage.jasdb;

import com.oberasoftware.home.api.storage.model.PluginItem;
import nl.renarj.jasdb.api.EmbeddedEntity;
import nl.renarj.jasdb.api.SimpleEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author renarj
 */
public class PluginEntityMapper implements EntityMapper<PluginItem> {
    @Override
    public SimpleEntity mapFrom(PluginItem plugin) {
        SimpleEntity pluginEntity = new SimpleEntity(plugin.getId());
        pluginEntity.addProperty("type", "plugin");
        pluginEntity.addProperty("pluginId", plugin.getPluginId());
        pluginEntity.addProperty("name", plugin.getName());
        pluginEntity.addProperty("controllerId", plugin.getControllerId());

        EmbeddedEntity deviceProperties = new EmbeddedEntity();
        plugin.getProperties().forEach(deviceProperties::addProperty);
        pluginEntity.addEntity("pluginProperties", deviceProperties);

        return pluginEntity;
    }

    @Override
    public PluginItem mapTo(SimpleEntity entity) {
        String controllerId = entity.getValue("controllerId");
        String pluginId = entity.getValue("pluginId");
        String name = entity.getValue("name");

        Map<String, String> properties = new HashMap<>();
        SimpleEntity pluginProperties = entity.getEntity("pluginProperties");
        pluginProperties.getProperties().forEach(p -> properties.put(p.getPropertyName(), p.getFirstValueObject()));

        return new PluginItem(entity.getInternalId(), controllerId, pluginId, name, properties);
    }
}
