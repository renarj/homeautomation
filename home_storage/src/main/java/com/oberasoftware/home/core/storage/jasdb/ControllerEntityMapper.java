package com.oberasoftware.home.core.storage.jasdb;

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

        controllerItem.getPluginIds().forEach(p -> controllerEntity.addProperty("plugins", p));

        return controllerEntity;
    }

    @Override
    public ControllerItem mapTo(SimpleEntity entity) {
        String controllerId = entity.getValue("controllerId");
        ControllerItem controllerItem = new ControllerItem(entity.getInternalId(), controllerId);
        if(entity.hasProperty("plugins")) {
            entity.getProperty("plugins").getValues().forEach(v -> controllerItem.addPluginId(v.toString()));
        }

        return controllerItem;
    }
}
