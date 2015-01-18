package com.oberasoftware.home.storage.jasdb;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class JasDBCentralDatastore implements CentralDatastore {
    private static final Logger LOG = getLogger(JasDBCentralDatastore.class);

    public static final String DEVICE_TYPE = "device";
    public static final String PLUGIN_TYPE = "plugin";
    public static final String CONTROLLER_TYPE = "controller";

    private static final String ITEMS_BAG_NAME = "items";

    private static final EntityMapperFactory MAPPER_FACTORY = new EntityMapperFactory();



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
            createSession().createOrGetBag(ITEMS_BAG_NAME).ensureIndex(
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
        createOrUpdate(new EntityMapperFactory().mapFrom(entity), ITEMS_BAG_NAME);

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
            EntityBag bag = session.createOrGetBag(ITEMS_BAG_NAME);

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
        return findItem(new ImmutableMap.Builder<String, String>()
                .put("controllerId", controllerId)
                .put("type", CONTROLLER_TYPE).build());
    }

    @Override
    public Optional<PluginItem> findPlugin(String controllerId, String pluginId) {
        return findItem(new ImmutableMap.Builder<String, String>()
                .put("controllerId", controllerId)
                .put("pluginId", pluginId)
                .put("type", PLUGIN_TYPE).build());
    }


    @Override
    public Optional<DeviceItem> findDevice(String controllerId, String pluginId, String deviceId) {
        return findItem(new ImmutableMap.Builder<String, String>()
                .put("controllerId", controllerId)
                .put("pluginId", pluginId)
                .put("deviceId", deviceId)
                .put("type", DEVICE_TYPE).build());
    }

    @Override
    public List<DeviceItem> findDevices() {
        return findItems(new ImmutableMap.Builder<String, String>()
                .put("type", DEVICE_TYPE).build());
    }

    private <T> Optional<T> findItem(Map<String, String> properties) {
        List<T> items = findItems(properties);
        return ofNullable(Iterables.getFirst(items, null));
    }

    private <T> List<T> findItems(Map<String, String> properties) {
        List<T> results = new ArrayList<>();

        try {
            DBSession session = createSession();

            EntityBag bag = session.createOrGetBag(ITEMS_BAG_NAME);

            QueryBuilder queryBuilder = QueryBuilder.createBuilder();
            properties.forEach((k, v) -> queryBuilder.field(k).value(v));

            QueryResult result = bag.find(queryBuilder).execute();
            LOG.debug("Query: {} results: {}", queryBuilder, result.size());

            result.forEach(r -> results.add(MAPPER_FACTORY.mapTo(r)));
        } catch (JasDBStorageException e) {
            LOG.error("Unable to query JasDB", e);
        }
        return results;
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
