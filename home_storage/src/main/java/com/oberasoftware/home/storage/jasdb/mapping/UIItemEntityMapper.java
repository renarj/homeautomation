package com.oberasoftware.home.storage.jasdb.mapping;

import com.oberasoftware.home.api.storage.model.UIItem;
import com.oberasoftware.home.storage.jasdb.JasDBCentralDatastore;
import nl.renarj.jasdb.api.SimpleEntity;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

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

        SimpleEntity itemEntity = new SimpleEntity(item.getId());
        itemEntity.addProperty("type", JasDBCentralDatastore.UI_TYPE);
        itemEntity.addProperty("name", item.getName());
        itemEntity.addProperty("description", item.getDescription());
        itemEntity.addProperty("uiType", item.getUiType());
        itemEntity.addProperty("deviceId", item.getDeviceId());

        return itemEntity;
    }

    @Override
    public UIItem mapTo(SimpleEntity entity) {
        LOG.debug("Converting entity: {}", entity);
        String name = entity.getValue("name");
        String description = entity.getValue("description");
        String uiType = entity.getValue("uiType");
        String deviceId = entity.getValue("deviceId");

        return new UIItem(entity.getInternalId(), name, description, uiType, deviceId);
    }
}
