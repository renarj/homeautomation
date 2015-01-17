package com.oberasoftware.home.storage.jasdb;

import com.oberasoftware.home.api.storage.model.ControllerItem;
import nl.renarj.jasdb.api.SimpleEntity;

/**
 * @author renarj
 */
public class ControllerEntityMapper implements EntityMapper<ControllerItem> {
    @Override
    public SimpleEntity mapFrom(ControllerItem controllerItem) {
        SimpleEntity controllerEntity = new SimpleEntity(controllerItem.getId());
        controllerEntity.addProperty("type", "controller");
        controllerEntity.addProperty("controllerId", controllerItem.getControllerId());

        return controllerEntity;
    }

    @Override
    public ControllerItem mapTo(SimpleEntity entity) {
        String controllerId = entity.getValue("controllerId");

        return new ControllerItem(entity.getInternalId(), controllerId);
    }
}
