package com.oberasoftware.home.rest;

import com.oberasoftware.home.api.managers.GroupManager;
import com.oberasoftware.home.api.model.storage.GroupItem;
import com.oberasoftware.home.core.model.storage.GroupItemImpl;
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
@RequestMapping("/groups")
public class GroupController {
    private static final Logger LOG = getLogger(GroupController.class);

    @Autowired
    private GroupManager groupManager;

    @RequestMapping(value = "/")
    public List<GroupItem> findAllGroups() {
        return groupManager.getGroups();
    }

    @RequestMapping(value = "/groups(groupId)")
    public GroupItem findGroup(@PathVariable String groupId) {
        return groupManager.getGroup(groupId);
    }


    @RequestMapping(value = "/groups/controller({controllerId})")
    public List<GroupItem> findGroupsByController(@PathVariable String controllerId) {
        return groupManager.getGroups(controllerId);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public GroupItem createGroup(@RequestBody GroupItemImpl item) {
        return groupManager.store(item);
    }

    @RequestMapping(value = "/groups({groupId})", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    public void deleteGroup(@PathVariable String groupId) {
        LOG.debug("Deleting group: {}", groupId);
        groupManager.delete(groupId);
        LOG.debug("Deleted group");
    }


}
