package com.oberasoftware.home.rest;

import com.oberasoftware.home.api.managers.VirtualItemManager;
import com.oberasoftware.home.api.model.storage.VirtualItem;
import com.oberasoftware.home.core.model.storage.VirtualItemImpl;
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
 * @author Renze de Vries
 */
@RestController
@RequestMapping("/virtualitems")
public class VirtualItemController {
    private static final Logger LOG = getLogger(VirtualItemController.class);

    @Autowired
    private VirtualItemManager virtualItemManager;

    @RequestMapping(value = "/")
    public List<? extends VirtualItem> findAllItems() {
        return virtualItemManager.getItems();
    }

    @RequestMapping(value = "/items(itemId)")
    public VirtualItem findItem(@PathVariable String itemId) {
        return virtualItemManager.getItem(itemId);
    }


    @RequestMapping(value = "/items/controller({controllerId})")
    public List<? extends VirtualItem> findItemsByController(@PathVariable String controllerId) {
        return virtualItemManager.getItems(controllerId);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public VirtualItem createItem(@RequestBody VirtualItemImpl item) {
        return virtualItemManager.store(item);
    }

    @RequestMapping(value = "/items({itemId})", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    public void deleteItem(@PathVariable String itemId) {
        LOG.debug("Deleting Virtual Item: {}", itemId);
        try {
            virtualItemManager.delete(itemId);
        } catch(Exception e) {
            LOG.error("", e);
        }
        LOG.debug("Deleted Virtual Item");
    }


}
