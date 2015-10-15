package com.oberasoftware.home.rest;

import com.oberasoftware.home.api.managers.UIManager;
import com.oberasoftware.home.api.model.storage.Container;
import com.oberasoftware.home.api.model.storage.Widget;
import com.oberasoftware.home.core.model.storage.ContainerImpl;
import com.oberasoftware.home.core.model.storage.WidgetImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    @ResponseStatus(HttpStatus.OK)
    public void deleteContainer(@PathVariable String containerId) {
        uiManager.deleteContainer(containerId);
    }

    @RequestMapping("/dashboard({dashboardId})/containers")
    public List<Container> getDashboardContainers(@PathVariable String dashboardId) {
        return uiManager.getDashboardContainers(dashboardId);
    }

    @RequestMapping("/containers({containerId})/items")
    public List<Widget> getItems(@PathVariable String containerId) {
        return uiManager.getItems(containerId);
    }

    @RequestMapping(value = "/items", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Widget createItem(@RequestBody WidgetImpl item) {
        return uiManager.store(item);
    }

    @RequestMapping(value = "/items({itemId})", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItem(@PathVariable String itemId) {
        uiManager.deleteWidget(itemId);
    }

    @RequestMapping(value = "/items/({itemId})/setParent({containerId})", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public void setParentContainer(@PathVariable String itemId, @PathVariable String containerId) {
        LOG.debug("Setting item: {} parent container: {}", itemId, containerId);
        uiManager.setParentContainer(itemId, containerId);
    }

    @RequestMapping(value = "/items/({itemId})/setProperty({property},{value})", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void setProperty(@PathVariable String itemId, @PathVariable String property, @PathVariable String value) {
        LOG.debug("Setting item: {} property: {} to value: {}", itemId, property, value);
        uiManager.setWidgetProperty(itemId, property, value);
    }

    @RequestMapping(value = "/containers", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Container createContainer(@RequestBody ContainerImpl container) {
        LOG.debug("Container creation: {}", container);
        return uiManager.store(container);
    }
}
