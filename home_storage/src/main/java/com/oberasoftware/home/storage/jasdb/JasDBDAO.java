package com.oberasoftware.home.storage.jasdb;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.oberasoftware.home.api.storage.HomeDAO;
import com.oberasoftware.home.api.storage.model.Container;
import com.oberasoftware.home.api.storage.model.ControllerItem;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.api.storage.model.Item;
import com.oberasoftware.home.api.storage.model.PluginItem;
import com.oberasoftware.home.api.storage.model.UIItem;
import com.oberasoftware.home.storage.jasdb.mapping.EntityMapperFactory;
import nl.renarj.jasdb.api.model.EntityBag;
import nl.renarj.jasdb.api.query.QueryBuilder;
import nl.renarj.jasdb.api.query.QueryResult;
import nl.renarj.jasdb.core.exceptions.JasDBStorageException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.oberasoftware.home.storage.jasdb.JasDBCentralDatastore.*;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class JasDBDAO implements HomeDAO {
    private static final Logger LOG = getLogger(JasDBDAO.class);

    @Autowired
    private JasDBSessionFactory sessionFactory;

    @Autowired
    private EntityMapperFactory mapperFactory;

    @Override
    public <T extends Item> Optional<T> findItem(String id) {
        try {
            EntityBag bag = sessionFactory.createSession().createOrGetBag(ITEMS_BAG_NAME);

            return Optional.of(mapperFactory.mapTo(bag.getEntity(id)));
        } catch(JasDBStorageException e) {
            LOG.error("", e);
        }
        return Optional.empty();
    }

    @Override
    public <T extends Container> Optional<T> findContainer(String id) {
        try {
            EntityBag bag = sessionFactory.createSession().createOrGetBag(ITEMS_BAG_NAME);

            return Optional.of(mapperFactory.mapTo(bag.getEntity(id)));
        } catch(JasDBStorageException e) {
            LOG.error("", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Container> findRootContainers() {
        return findItems(new ImmutableMap.Builder<String, String>()
                .put("type", CONTAINER_TYPE)
                .put("parent", "").build());
    }

    @Override
    public List<Container> findContainers() {
        return findItems(new ImmutableMap.Builder<String, String>()
                .put("type", CONTAINER_TYPE).build());
    }

    @Override
    public List<Container> findContainers(String parentId) {
        return findItems(new ImmutableMap.Builder<String, String>()
                .put("type", CONTAINER_TYPE)
                .put("parent", parentId).build());
    }

    @Override
    public List<UIItem> findUIItems(String containerId) {
        return findItems(new ImmutableMap.Builder<String, String>()
                .put("type", UI_TYPE)
                .put("containerId", containerId).build(), newArrayList("weight"));
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
    public List<ControllerItem> findControllers() {
        return findItems(new ImmutableMap.Builder<String, String>()
                .put("type", CONTROLLER_TYPE).build());
    }

    @Override
    public List<PluginItem> findPlugins(String controllerId) {
        return findItems(new ImmutableMap.Builder<String, String>()
                .put("controllerId", controllerId)
                .put("type", PLUGIN_TYPE).build());
    }

    @Override
    public List<DeviceItem> findDevices(String controllerId, String pluginId) {
        return findItems(new ImmutableMap.Builder<String, String>()
                .put("controllerId", controllerId)
                .put("pluginId", pluginId)
                .put("type", DEVICE_TYPE).build());
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
        return findItems(properties, new ArrayList<>());
    }

    private <T> List<T> findItems(Map<String, String> properties, List<String> orderedBy) {
        List<T> results = new ArrayList<>();

        try {
            EntityBag bag = sessionFactory.createSession().createOrGetBag(ITEMS_BAG_NAME);

            QueryBuilder queryBuilder = QueryBuilder.createBuilder();
            properties.forEach((k, v) -> queryBuilder.field(k).value(v));
            orderedBy.forEach(queryBuilder::sortBy);

            QueryResult result = bag.find(queryBuilder).execute();
            LOG.debug("Query: {} results: {}", queryBuilder, result.size());

            result.forEach(r -> results.add(mapperFactory.mapTo(r)));
        } catch (JasDBStorageException e) {
            LOG.error("Unable to query JasDB", e);
        }
        return results;
    }
}
