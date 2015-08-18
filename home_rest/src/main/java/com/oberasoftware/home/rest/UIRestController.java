package com.oberasoftware.home.rest;

import com.oberasoftware.home.api.managers.UIManager;
import com.oberasoftware.home.api.model.storage.Container;
import com.oberasoftware.home.core.model.storage.ContainerImpl;
import com.oberasoftware.home.api.model.storage.UIItem;
import com.oberasoftware.home.core.model.storage.UIItemImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@RestController
@RequestMapping("/ui")
public class UIRestController {
    private static final Logger LOG = getLogger(UIRestController.class);

    @Autowired
    private UIManager uiManager;

    @RequestMapping("/containers({containerId})")
    public Container getContainer(@PathVariable String containerId) {
        return uiManager.getContainer(containerId);
    }

    @RequestMapping("/containers")
    public List<Container> getContainers() {
        return uiManager.getAllContainers();
    }

    @RequestMapping("/containers({containerId})/children")
    public List<Container> getContainers(@PathVariable String containerId) {
        return uiManager.getChildren(containerId);
    }

    @RequestMapping(value = "/containers({containerId})", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    public void deleteContainer(@PathVariable String containerId) {
        uiManager.deleteContainer(containerId);
    }

    @RequestMapping("/dashboard({dashboardId})/containers")
    public List<Container> getDashboardContainers(@PathVariable String dashboardId) {
        return uiManager.getDashboardContainers(dashboardId);
    }

    @RequestMapping("/containers({containerId})/items")
    public List<UIItem> getItems(@PathVariable String containerId) {
        try {
            return uiManager.getItems(containerId);
        } catch(Exception e) {
            LOG.error("", e);
            return new ArrayList<>();
        }
    }

    @RequestMapping(value = "/items", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public UIItem createItem(@RequestBody UIItemImpl item) {
        try {
            return uiManager.store(item);
        } catch(Exception e) {
            LOG.error("", e);

            throw e;
        }
    }

    @RequestMapping(value = "/items({itemId})", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    public void deleteItem(@PathVariable String itemId) {
        uiManager.deleteWidget(itemId);
    }

    @RequestMapping(value = "/items({itemId})/setWeight({weight})", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public void setItemWeight(@PathVariable String itemId, @PathVariable long weight) {
        LOG.debug("Setting item: {} weight: {}", itemId, weight);
        uiManager.setWeight(itemId, weight);
    }

    @RequestMapping(value = "/items/({itemId})/setParent({containerId})", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public void setParentContainer(@PathVariable String itemId, @PathVariable String containerId) {
        LOG.debug("Setting item: {} parent container: {}", itemId, containerId);
        uiManager.setParentContainer(itemId, containerId);
    }


    @RequestMapping(value = "/containers", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Container createContainer(@RequestBody ContainerImpl container) {
        LOG.debug("Container creation: {}", container);
        return uiManager.store(container);
    }
}
