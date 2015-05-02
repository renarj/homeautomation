package com.oberasoftware.home.service;

import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.managers.UIManager;
import com.oberasoftware.home.api.storage.CentralDatastore;
import com.oberasoftware.home.api.storage.HomeDAO;
import com.oberasoftware.home.api.storage.model.Container;
import com.oberasoftware.home.api.storage.model.UIItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public List<Container> getRootContainers() {
        return homeDAO.findRootContainers();
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
        return homeDAO.findContainer(containerId).get();
    }

    @Override
    public List<UIItem> getItems(String containerId) {
        return homeDAO.findUIItems(containerId);
    }

    @Override
    public void delete(String itemId) {
        try {
            centralDatastore.delete(itemId);
        } catch (DataStoreException e) {
            LOG.error("", e);
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
