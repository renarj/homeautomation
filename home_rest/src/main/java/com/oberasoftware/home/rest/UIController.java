package com.oberasoftware.home.rest;

import com.oberasoftware.home.api.managers.UIManager;
import com.oberasoftware.home.api.storage.model.Container;
import com.oberasoftware.home.api.storage.model.UIItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@RestController
@RequestMapping("/ui")
public class UIController {
    private static final Logger LOG = getLogger(UIController.class);

    @Autowired
    private UIManager uiManager;

    @RequestMapping("/containers({containerId})")
    public Container getContainer(@PathVariable String containerId) {
        return uiManager.getContainer(containerId);
    }

    @RequestMapping("/containers({containerId})/children")
    public List<Container> getContainers(@PathVariable String containerId) {
        return uiManager.getChildren(containerId);
    }

    @RequestMapping(value = "/containers/{containerId}", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    public void deleteContainer(@PathVariable String containerId) {
        uiManager.deleteContainer(containerId);
    }

    @RequestMapping("/containers")
    public List<Container> getContainers() {
        return uiManager.getRootContainers();
    }

    @RequestMapping("/containers/{containerId}/items")
    public List<UIItem> getItems(@PathVariable String containerId) {
        return uiManager.getItems(containerId);
    }

    @RequestMapping(value = "/items", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public UIItem createItem(@RequestBody UIItem item) {
        return uiManager.store(item);
    }

    @RequestMapping(value = "/items/{itemId}", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    public void deleteItem(@PathVariable String itemId) {
        uiManager.delete(itemId);
    }

    @RequestMapping(value = "/items/{itemId}/{weight}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public void deleteItem(@PathVariable String itemId, @PathVariable long weight) {
        LOG.debug("Setting item: {} weight: {}", itemId, weight);
        uiManager.setWeight(itemId, weight);
    }


    @RequestMapping(value = "/containers", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Container createContainer(@RequestBody Container container) {
        return uiManager.store(container);
    }
}
