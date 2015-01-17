package com.oberasoftware.home.storage.jasdb;

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
import nl.renarj.jasdb.index.keys.types.StringKeyType;
import nl.renarj.jasdb.index.search.CompositeIndexField;
import nl.renarj.jasdb.index.search.IndexField;
import nl.renarj.jasdb.rest.client.ResourceNotFoundException;
import nl.renarj.jasdb.rest.client.RestDBSession;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class JasDBCentralDatastore implements CentralDatastore {
    private static final Logger LOG = getLogger(JasDBCentralDatastore.class);

    private Lock lock = new ReentrantLock();

    @Override
    public void beginTransaction() {
        LOG.debug("Locking DB access");
        lock.lock();
    }

    @Override
    public void commitTransaction() {
        LOG.debug("Unlock DB access");
        lock.unlock();
    }

    @PostConstruct
    public void createIndexOnStartup() {
        LOG.debug("Creating a composite index");

        try {
            createSession().createOrGetBag("items").ensureIndex(
                    new CompositeIndexField(
                            new IndexField("controllerId", new StringKeyType()),
                            new IndexField("pluginId", new StringKeyType()),
                            new IndexField("deviceId", new StringKeyType()),
                            new IndexField("type", new StringKeyType())
                    ), false);
        } catch (JasDBStorageException e) {
            LOG.error("", e);
        }
    }

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

            boolean exists = false;

            try {
                SimpleEntity e = bag.getEntity(entity.getInternalId());
                if(e != null) {
                    exists = true;
                }
            } catch(ResourceNotFoundException e) {

            }

            if(exists) {
                LOG.debug("Entity already exists, updating: {}", entity);
                bag.updateEntity(entity);
            } else {
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
    public Optional<PluginItem> findPlugin(String controllerId, String pluginId) {
        Map<String, String> properties = Maps.newLinkedHashMap();
        properties.put("controllerId", controllerId);
        properties.put("pluginId", pluginId);
        properties.put("type", "plugin");

        return findItem(properties);
    }


    @Override
    public Optional<DeviceItem> findDevice(String controllerId, String pluginId, String deviceId) {
        Map<String, String> properties = Maps.newLinkedHashMap();
        properties.put("controllerId", controllerId);
        properties.put("pluginId", pluginId);
        properties.put("deviceId", deviceId);
        properties.put("type", "device");

        return findItem(properties);
    }

    private <T> Optional<T> findItem(Map<String, String> properties) {
        try {
            DBSession session = createSession();

            EntityBag bag = session.createOrGetBag("items");

            QueryBuilder queryBuilder = QueryBuilder.createBuilder();
            properties.forEach((k, v) -> queryBuilder.field(k).value(v));

            QueryResult result = bag.find(queryBuilder).execute();
            LOG.debug("Query: {} results: {}", queryBuilder, result.size());
            if(result.size() != 0) {
                SimpleEntity entity = Iterables.getFirst(result, null);
                LOG.debug("Found an item: {} of type: {}", entity, entity != null ? entity.getValue("type").toString() : "unknown");

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
//            return new LocalDBSession();
        } catch (JasDBStorageException e) {
            throw new RuntimeHomeAutomationException("", e);
        }
    }
}
