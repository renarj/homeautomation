package com.oberasoftware.home.storage.jasdb.mapping;

import com.oberasoftware.home.api.storage.model.UIItem;
import com.oberasoftware.home.storage.jasdb.JasDBCentralDatastore;
import nl.renarj.core.utilities.StringUtils;
import nl.renarj.jasdb.api.EmbeddedEntity;
import nl.renarj.jasdb.api.SimpleEntity;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class UIItemEntityMapper implements EntityMapper<UIItem> {
    private static final Logger LOG = getLogger(UIItemEntityMapper.class);

    @Override
    public SimpleEntity mapFrom(UIItem item) {
        LOG.debug("Converting item: {}", item);

        String id = item.getId();
        if(StringUtils.stringEmpty(id)) {
            id = UUID.randomUUID().toString();
        }

        SimpleEntity itemEntity = new SimpleEntity(id);
        itemEntity.addProperty("type", JasDBCentralDatastore.UI_TYPE);
        itemEntity.addProperty("name", item.getName());
        itemEntity.addProperty("description", item.getDescription());
        itemEntity.addProperty("containerId", item.getContainerId());
        itemEntity.addProperty("uiType", item.getUiType());
        itemEntity.addProperty("deviceId", item.getDeviceId());

        EmbeddedEntity itemProperties = new EmbeddedEntity();
        item.getProperties().forEach(itemProperties::addProperty);
        itemEntity.addEntity("itemProperties", itemProperties);

        return itemEntity;
    }

    @Override
    public UIItem mapTo(SimpleEntity entity) {
        LOG.debug("Converting entity: {}", entity);
        String name = entity.getValue("name");
        String description = entity.getValue("description");
        String uiType = entity.getValue("uiType");
        String deviceId = entity.getValue("deviceId");
        String containerId = entity.hasProperty("containerId") ? entity.getValue("containerId") : null;

        Map<String, String> properties = new HashMap<>();
        SimpleEntity itemProperties = entity.getEntity("itemProperties");
        itemProperties.getProperties().forEach(p -> properties.put(p.getPropertyName(), p.getFirstValueObject()));


        return new UIItem(entity.getInternalId(), name, containerId, description, uiType, deviceId, properties);
    }
}
