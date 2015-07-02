package com.oberasoftware.home.web;

import com.oberasoftware.home.api.managers.GroupManager;
import com.oberasoftware.home.api.managers.ItemManager;
import com.oberasoftware.home.api.model.storage.ControllerItem;
import com.oberasoftware.home.api.model.storage.GroupItem;
import com.oberasoftware.home.api.model.storage.PluginItem;
import com.oberasoftware.home.web.model.WebGroup;
import com.oberasoftware.home.web.model.WebPluginItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Renze de Vries
 */
@Controller
@RequestMapping("/web/admin/groups")
public class GroupAdminController {

    @Autowired
    private ItemManager itemManager;

    @Autowired
    private GroupManager groupManager;

    @RequestMapping
    public String getGroups(Model model) {
        List<ControllerItem> controllers = itemManager.findControllers();
        model.addAttribute("controllers", controllers);

        return "admin/groups";
    }

    @RequestMapping("/{controllerId}")
    public String getGroups(@PathVariable String controllerId, Model model) {
        List<ControllerItem> controllers = itemManager.findControllers();
        List<GroupItem> groupItems = groupManager.getGroups(controllerId);
        List<PluginItem> plugins = itemManager.findPlugins(controllerId);
        List<WebPluginItem> webPluginItems = plugins.stream()
                .map(p -> new WebPluginItem(p, itemManager.findDevices(controllerId, p.getPluginId())))
                .collect(Collectors.toList());
        List<WebGroup> webGroups = groupItems.stream()
                .map(g -> new WebGroup(g, groupManager.getDevices(g.getId()))).collect(Collectors.toList());

        model.addAttribute("controllers", controllers);
        model.addAttribute("groups", webGroups);
        model.addAttribute("selectedController", controllerId);
        model.addAttribute("plugins", webPluginItems);

        return "admin/groups";
    }


}
