package com.oberasoftware.home.web;

import com.oberasoftware.home.api.managers.ItemManager;
import com.oberasoftware.home.api.managers.VirtualItemManager;
import com.oberasoftware.home.api.model.storage.ControllerItem;
import com.oberasoftware.home.api.model.storage.VirtualItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Renze de Vries
 */
@Controller
@RequestMapping("/web/admin/virtualitems")
public class VirtualItemAdminController {

    @Autowired
    private ItemManager itemManager;

    @Autowired
    private VirtualItemManager virtualItemManager;

    @RequestMapping
    public String getItems(Model model) {
        List<ControllerItem> controllers = itemManager.findControllers();
        model.addAttribute("controllers", controllers);

        return "admin/virtualitems";
    }

    @RequestMapping("/{controllerId}")
    public String getGroups(@PathVariable String controllerId, Model model) {
        List<ControllerItem> controllers = itemManager.findControllers();
        List<? extends VirtualItem> virtualItems = virtualItemManager.getItems(controllerId);

        model.addAttribute("controllers", controllers);
        model.addAttribute("items", virtualItems);
        model.addAttribute("selectedController", controllerId);

        return "admin/virtualitems";
    }


}
