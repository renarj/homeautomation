package com.oberasoftware.home.storage.jasdb.mapping;

import com.oberasoftware.home.api.storage.model.ControllerItem;
import com.oberasoftware.home.storage.jasdb.JasDBCentralDatastore;
import nl.renarj.jasdb.api.SimpleEntity;
import org.springframework.stereotype.Component;

/**
 * @author renarj
 */
@Component
public class ControllerEntityMapper implements EntityMapper<ControllerItem> {
    @Override
    public SimpleEntity mapFrom(ControllerItem controllerItem) {
        SimpleEntity controllerEntity = new SimpleEntity(controllerItem.getId());
        controllerEntity.addProperty("type", JasDBCentralDatastore.CONTROLLER_TYPE);
        controllerEntity.addProperty("controllerId", controllerItem.getControllerId());

        return controllerEntity;
    }

    @Override
    public ControllerItem mapTo(SimpleEntity entity) {
        String controllerId = entity.getValue("controllerId");

        return new ControllerItem(entity.getInternalId(), controllerId);
    }
}
