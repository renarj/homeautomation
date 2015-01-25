package com.oberasoftware.home.storage.jasdb;

import com.oberasoftware.home.api.storage.model.DeviceItem;
import nl.renarj.jasdb.api.EmbeddedEntity;
import nl.renarj.jasdb.api.SimpleEntity;
import nl.renarj.jasdb.api.properties.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author renarj
 */
public class DeviceEntityMapper implements EntityMapper<DeviceItem> {
    @Override
    public SimpleEntity mapFrom(DeviceItem item) {
        SimpleEntity deviceEntity = new SimpleEntity(item.getId());

        deviceEntity.addProperty("type", JasDBCentralDatastore.DEVICE_TYPE);
        deviceEntity.addProperty("deviceId", item.getDeviceId());
        deviceEntity.addProperty("name", item.getName());
        deviceEntity.addProperty("pluginId", item.getPluginId());
        deviceEntity.addProperty("controllerId", item.getControllerId());

        EmbeddedEntity deviceProperties = new EmbeddedEntity();
        item.getProperties().forEach(deviceProperties::addProperty);
        deviceEntity.addEntity("deviceProperties", deviceProperties);

        EmbeddedEntity configurationProperties = new EmbeddedEntity();
        item.getConfiguration().forEach(configurationProperties::addProperty);
        deviceEntity.addEntity("configuration", configurationProperties);

        return deviceEntity;
    }

    @Override
    public DeviceItem mapTo(SimpleEntity entity) {
        String pluginId = entity.getValue("pluginId");
        String deviceId = entity.getValue("deviceId");
        String name = entity.getValue("name");
        String controllerId = entity.getValue("controllerId");

        Map<String, String> properties = new HashMap<>();
        SimpleEntity deviceProperties = entity.getEntity("deviceProperties");
        deviceProperties.getProperties().forEach(p -> properties.put(p.getPropertyName(), p.getFirstValueObject()));

        Map<String, List<String>> configuration = new HashMap<>();
        SimpleEntity configProperties = entity.getEntity("configuration");
        if(configProperties != null) {
            configProperties.getProperties().forEach(p -> configuration.put(p.getPropertyName(), p.getValues().stream().map(Value::toString).collect(Collectors.toList())));
        }

        return new DeviceItem(entity.getInternalId(), controllerId, pluginId, deviceId, name, properties, configuration);
    }
}
