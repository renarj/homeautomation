package com.oberasoftware.home.service;

import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.exceptions.RuntimeHomeAutomationException;
import com.oberasoftware.home.api.managers.UIManager;
import com.oberasoftware.home.api.model.storage.Container;
import com.oberasoftware.home.api.model.storage.UIItem;
import com.oberasoftware.home.api.storage.CentralDatastore;
import com.oberasoftware.home.api.storage.HomeDAO;
import com.oberasoftware.home.core.model.storage.ContainerImpl;
import com.oberasoftware.home.core.model.storage.UIItemImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class UIManagerImpl implements UIManager {
    private static final Logger LOG = getLogger(UIManagerImpl.class);

    @Autowired
    private CentralDatastore centralDatastore;

    @Autowired
    private HomeDAO homeDAO;

    @Override
    public List<Container> getDashboardContainers(String dashboardId) {
        return homeDAO.findDashboardContainers(dashboardId);
    }

    @Override
    public List<Container> getAllContainers() {
        return homeDAO.findContainers();
    }

    @Override
    public List<Container> getChildren(String containerId) {
        return homeDAO.findContainers(containerId);
    }

    @Override
    public Container getContainer(String containerId) {
        try {
            return homeDAO.findContainer(containerId).get();
        } catch(Exception e) {
            LOG.error("", e);
            throw new RuntimeHomeAutomationException("Error", e);
        }
    }

    @Override
    public List<UIItem> getItems(String containerId) {
        return homeDAO.findUIItems(containerId);
    }

    @Override
    public void setWeight(String itemId, long weight) {
        centralDatastore.beginTransaction();
        try {
            Optional<UIItemImpl> item = homeDAO.findItem(UIItemImpl.class, itemId);
            if (item.isPresent()) {
                UIItemImpl uiItem = item.get();
                uiItem.setWeight(weight);
                store(uiItem);
            }
        } finally {
            centralDatastore.commitTransaction();
        }
    }


    @Override
    public void setParentContainer(String itemId, String parentContainerId) {
        centralDatastore.beginTransaction();
        try {
            Optional<UIItemImpl> item = homeDAO.findItem(UIItemImpl.class, itemId);
            if (item.isPresent()) {
                UIItemImpl uiItem = item.get();
                uiItem.setContainerId(parentContainerId);
                store(uiItem);
            }
        } finally {
            centralDatastore.commitTransaction();
        }
    }


    @Override
    public void deleteContainer(String containerId) {
        centralDatastore.beginTransaction();
        try {
            List<Container> children = getChildren(containerId);
            children.forEach(c -> deleteContainer(c.getId()));

            getItems(containerId).forEach(i -> delete(UIItemImpl.class, i.getId()));
            delete(ContainerImpl.class, containerId);
        } finally {
            centralDatastore.commitTransaction();
        }
    }

    @Override
    public void deleteWidget(String itemId) {
        delete(UIItemImpl.class, itemId);
    }

    private void delete(Class<?> type, String itemId) {
        centralDatastore.beginTransaction();
        try {

            centralDatastore.delete(type, itemId);
        } catch (DataStoreException e) {
            LOG.error("", e);
        } finally {
            centralDatastore.commitTransaction();
        }
    }

    @Override
    public UIItem store(UIItem item) {
        try {
            return centralDatastore.store(item);
        } catch (DataStoreException e) {
            LOG.error("", e);
        }
        return null;
    }

    @Override
    public Container store(Container container) {
        try {
            return centralDatastore.store(container);
        } catch (DataStoreException e) {
            LOG.error("", e);
        }
        return null;
    }
}
