package com.oberasoftware.home.core.storage.jasdb;

import com.oberasoftware.home.api.storage.model.DevicePlugin;
import nl.renarj.jasdb.api.SimpleEntity;

/**
 * @author renarj
 */
public class PluginEntityMapper implements EntityMapper<DevicePlugin> {
    @Override
    public SimpleEntity mapFrom(DevicePlugin plugin) {
        SimpleEntity pluginEntity = new SimpleEntity(plugin.getId());
        pluginEntity.addProperty("type", "plugin");
        pluginEntity.addProperty("pluginId", plugin.getPluginId());
        pluginEntity.addProperty("name", plugin.getName());
        pluginEntity.addProperty("controllerId", plugin.getControllerId());
        plugin.getDeviceIds().forEach(d -> pluginEntity.addProperty("devices", d));

        return pluginEntity;
    }

    @Override
    public DevicePlugin mapTo(SimpleEntity entity) {
        String controllerId = entity.getValue("controllerId");
        String pluginId = entity.getValue("pluginId");
        String name = entity.getValue("name");

        DevicePlugin plugin = new DevicePlugin(entity.getInternalId(), controllerId, pluginId, name);

        if(entity.hasProperty("devices")) {
            entity.getProperty("devices").getValues().forEach(v -> plugin.addDevice(v.toString()));
        }

        return plugin;
    }
}
