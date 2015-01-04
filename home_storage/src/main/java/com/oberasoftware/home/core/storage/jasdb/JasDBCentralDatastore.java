package com.oberasoftware.home.core.storage.jasdb;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.exceptions.RuntimeHomeAutomationException;
import com.oberasoftware.home.api.storage.CentralDatastore;
import com.oberasoftware.home.api.storage.model.*;
import nl.renarj.jasdb.api.DBSession;
import nl.renarj.jasdb.api.SimpleEntity;
import nl.renarj.jasdb.api.model.EntityBag;
import nl.renarj.jasdb.api.query.QueryBuilder;
import nl.renarj.jasdb.api.query.QueryResult;
import nl.renarj.jasdb.core.exceptions.JasDBStorageException;
import nl.renarj.jasdb.rest.client.ResourceNotFoundException;
import nl.renarj.jasdb.rest.client.RestDBSession;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class JasDBCentralDatastore implements CentralDatastore {
    private static final Logger LOG = getLogger(JasDBCentralDatastore.class);

    @Override
    public <T extends Item> T store(Item entity) throws DataStoreException {
        LOG.debug("Storing entity: {}", entity);
        createOrUpdate(new EntityMapperFactory().mapFrom(entity), "items");

        return (T)entity;
    }

    @Override
    public Container store(Container container) throws DataStoreException {
        return null;
    }

    private void createOrUpdate(SimpleEntity entity, String bagName) throws DataStoreException {
        try {
            DBSession session = createSession();
            EntityBag bag = session.createOrGetBag(bagName);

            try {
                bag.getEntity(entity.getInternalId());

                LOG.debug("Entity already exists, updating: {}", entity);
                bag.updateEntity(entity);
            } catch(ResourceNotFoundException e) {
                LOG.debug("Entity does not yet exist, creating: {}", entity);
                bag.addEntity(entity);
            }
        } catch (JasDBStorageException e) {
            throw new DataStoreException("Unable to store item: " + entity, e);
        }
    }

    @Override
    public <T extends Item> Optional<T> findItem(String id) {
        try {
            DBSession session = createSession();
            EntityBag bag = session.createOrGetBag("items");

            return Optional.of(new EntityMapperFactory().mapTo(bag.getEntity(id)));
        } catch(JasDBStorageException e) {
            LOG.error("", e);
        }
        return Optional.empty();
    }

    @Override
    public <T extends Container> Optional<T> findContainer(String id) {
        return null;
    }

    @Override
    public Optional<ControllerItem> findController(String controllerId) {
        Map<String, String> properties = Maps.newHashMap();
        properties.put("controllerId", controllerId);

        return findItem(properties);
    }

    @Override
    public Optional<DevicePlugin> findPlugin(String controllerId, String pluginId) {
        Map<String, String> properties = Maps.newHashMap();
        properties.put("controllerId", controllerId);
        properties.put("pluginId", pluginId);

        return findItem(properties);
    }


    @Override
    public Optional<DeviceItem> findDevice(String controllerId, String pluginId, String deviceId) {
        Map<String, String> properties = Maps.newHashMap();
        properties.put("controllerId", controllerId);
        properties.put("pluginId", pluginId);
        properties.put("deviceId", deviceId);

        return findItem(properties);
    }

    private <T> Optional<T> findItem(Map<String, String> properties) {
        try {
            DBSession session = createSession();

            EntityBag bag = session.createOrGetBag("items");

            QueryBuilder queryBuilder = QueryBuilder.createBuilder();
            properties.forEach((k, v) -> queryBuilder.field(k).value(v));

            QueryResult result = bag.find(queryBuilder).execute();
            if(result.size() != 0) {
                SimpleEntity entity = Iterables.getFirst(result, null);
                LOG.debug("Found an item: {}", entity);

                return Optional.of(new EntityMapperFactory().mapTo(entity));
            }
        } catch (JasDBStorageException e) {
            LOG.error("", e);
        }

        return Optional.empty();
    }

    private DBSession createSession() {
        try {
            return new RestDBSession("default", "localhost", 7050);
        } catch (JasDBStorageException e) {
            throw new RuntimeHomeAutomationException("", e);
        }
    }
}
