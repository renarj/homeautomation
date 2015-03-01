package com.oberasoftware.home.storage.jasdb;

import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.storage.CentralDatastore;
import com.oberasoftware.home.api.storage.HomeDAO;
import com.oberasoftware.home.api.storage.model.Container;
import com.oberasoftware.home.api.storage.model.ControllerItem;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.api.storage.model.Item;
import com.oberasoftware.home.api.storage.model.PluginItem;
import com.oberasoftware.home.api.storage.model.UIItem;
import com.oberasoftware.home.storage.jasdb.mapping.EntityMapperFactory;
import nl.renarj.jasdb.api.SimpleEntity;
import nl.renarj.jasdb.api.model.EntityBag;
import nl.renarj.jasdb.core.exceptions.JasDBStorageException;
import nl.renarj.jasdb.index.keys.types.StringKeyType;
import nl.renarj.jasdb.index.search.CompositeIndexField;
import nl.renarj.jasdb.index.search.IndexField;
import nl.renarj.jasdb.rest.client.ResourceNotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class JasDBCentralDatastore implements CentralDatastore {
    private static final Logger LOG = getLogger(JasDBCentralDatastore.class);

    public static final String DEVICE_TYPE = DeviceItem.class.getName();
    public static final String PLUGIN_TYPE = PluginItem.class.getName();
    public static final String CONTROLLER_TYPE = ControllerItem.class.getName();
    public static final String UI_TYPE = UIItem.class.getName();

    public static final String ITEMS_BAG_NAME = "items";

    @Autowired
    private EntityMapperFactory mapperFactory;

    @Autowired
    private JasDBSessionFactory jasDBSessionFactory;

    @Autowired
    private JasDBDAO jasDBDAO;

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
            jasDBSessionFactory.createSession().createOrGetBag(ITEMS_BAG_NAME).ensureIndex(
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
        createOrUpdate(mapperFactory.mapFrom(entity), ITEMS_BAG_NAME);

        return (T)entity;
    }

    @Override
    public Container store(Container container) throws DataStoreException {
        LOG.debug("Storing container: {}", container);
        createOrUpdate(mapperFactory.mapFrom(container), ITEMS_BAG_NAME);

        return container;
    }

    private void createOrUpdate(SimpleEntity entity, String bagName) throws DataStoreException {
        try {
            EntityBag bag = jasDBSessionFactory.createSession().createOrGetBag(bagName);

            boolean exists = false;

            try {
                SimpleEntity e = bag.getEntity(entity.getInternalId());
                if(e != null) {
                    exists = true;
                }
            } catch(ResourceNotFoundException ignored) {

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
    public HomeDAO getDAO() {
        return jasDBDAO;
    }
}
