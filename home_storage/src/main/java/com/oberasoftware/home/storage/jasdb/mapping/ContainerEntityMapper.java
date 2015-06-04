package com.oberasoftware.home.storage.jasdb.mapping;

import com.oberasoftware.home.api.storage.model.Container;
import com.oberasoftware.home.storage.jasdb.JasDBCentralDatastore;
import nl.renarj.core.utilities.StringUtils;
import nl.renarj.jasdb.api.SimpleEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author renarj
 */
@Component
public class ContainerEntityMapper implements EntityMapper<Container> {
    @Override
    public SimpleEntity mapFrom(Container input) {
        String id = input.getId();
        SimpleEntity containerEntity = new SimpleEntity(id);
        containerEntity.addProperty("type", JasDBCentralDatastore.CONTAINER_TYPE);
        if(StringUtils.stringNotEmpty(input.getParentContainerId())) {
            containerEntity.addProperty("parent", input.getParentContainerId());
        } else {
            containerEntity.addProperty("parent", "");
        }
        containerEntity.addProperty("name", input.getName());

        return containerEntity;
    }

    @Override
    public Container mapTo(SimpleEntity entity) {
        String name = entity.getValue("name");
        String parent = entity.hasProperty("parent") ? entity.getValue("parent") : null;

        return new Container(entity.getInternalId(), name, parent);
    }
}
