package com.oberasoftware.home.storage.jasdb.mapping;

import nl.renarj.jasdb.api.SimpleEntity;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class EntityMapperFactory {
    private static final Logger LOG = getLogger(EntityMapperFactory.class);

    @Autowired
    private List<EntityMapper> entityMappers;

    private Map<String, EntityMapper<?>> entityMapperMap = new HashMap<>();

    @PostConstruct
    public void loadMapperTypes() {
        entityMappers.forEach(e -> {
            Optional<Method> mapperMethod = Arrays.stream(e.getClass().getMethods())
                    .filter(m -> !m.isBridge())
                    .filter(m -> m.getName().equals("mapFrom"))
                    .findFirst();
            if(mapperMethod.isPresent()) {
                Class<?>[] parameterTypes = mapperMethod.get().getParameterTypes();
                if(parameterTypes.length == 1) {
                    Class<?> entityType = parameterTypes[0];
                    LOG.debug("Found an Entity mapper: {} for type: {}", e, entityType);
                    entityMapperMap.put(entityType.getName(), e);
                }
            }
        });
    }

    public <T> SimpleEntity mapFrom(T input) {
        String entityName = input.getClass().getName();
        if(entityMapperMap.containsKey(entityName)) {
            EntityMapper<T> mapper = (EntityMapper<T>) entityMapperMap.get(entityName);
            LOG.debug("Mapping type: {} using mapper: {}", input, mapper);

            return mapper.mapFrom(input);
        }
        return null;
    }

    public <T> T mapTo(SimpleEntity entity) {
        String entityType = entity.getValue("type");
        if(entityMapperMap.containsKey(entityType)) {
            EntityMapper<T> mapper = (EntityMapper<T>) entityMapperMap.get(entityType);
            LOG.debug("Mapping entity: {} using mapper: {}", entity, mapper);
            return mapper.mapTo(entity);
        }

        return null;
    }
}
