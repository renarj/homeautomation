package com.oberasoftware.home.storage.jasdb;

import com.oberasoftware.home.api.storage.model.ControllerItem;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.api.storage.model.PluginItem;
import nl.renarj.jasdb.api.SimpleEntity;

/**
 * @author renarj
 */
public class EntityMapperFactory {

    private EntityMapper<DeviceItem> deviceItemEntityMapper = new DeviceEntityMapper();
    private EntityMapper<ControllerItem> controllerItemEntityMapper = new ControllerEntityMapper();
    private EntityMapper<PluginItem> pluginEntityMapper = new PluginEntityMapper();

    public <T> SimpleEntity mapFrom(T input) {
        if(input instanceof PluginItem) {
            return pluginEntityMapper.mapFrom((PluginItem)input);
        } else if(input instanceof ControllerItem) {
            return controllerItemEntityMapper.mapFrom((ControllerItem) input);
        } else if(input instanceof DeviceItem) {
            return deviceItemEntityMapper.mapFrom((DeviceItem) input);
        }

        return null;
    }

    public <T> T mapTo(SimpleEntity entity) {
        String entityType = entity.getValue("type");
        switch(entityType) {
            case "plugin":
                return (T)pluginEntityMapper.mapTo(entity);
            case "controller":
                return (T)controllerItemEntityMapper.mapTo(entity);
            case "device":
                return (T)deviceItemEntityMapper.mapTo(entity);
        }

        return null;
    }
}
