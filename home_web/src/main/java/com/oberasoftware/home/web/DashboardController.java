package com.oberasoftware.home.web;

import com.oberasoftware.home.api.managers.UIManager;
import com.oberasoftware.home.api.storage.model.Container;
import com.oberasoftware.home.web.model.WebContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author renarj
 */
@Controller
@RequestMapping("/web")
public class DashboardController {
    @Autowired
    private UIManager uiManager;

    @RequestMapping
    public String getIndex(Model model) {
        List<Container> rootContainers = uiManager.getRootContainers();
        List<WebContainer> webContainers = rootContainers.stream()
                .map(c -> new WebContainer(c, uiManager.getItems(c.getId())))
                .collect(Collectors.toList());

        model.addAttribute("containers", webContainers);

        return "index";
    }

}
