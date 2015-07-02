package com.oberasoftware.home.web;

import com.google.common.io.ByteStreams;
import com.oberasoftware.home.api.extensions.AutomationExtension;
import com.oberasoftware.home.api.extensions.ExtensionManager;
import com.oberasoftware.home.api.managers.ItemManager;
import com.oberasoftware.home.api.managers.UIManager;
import com.oberasoftware.home.api.model.ExtensionResource;
import com.oberasoftware.home.api.model.storage.Container;
import com.oberasoftware.home.api.model.storage.ControllerItem;
import com.oberasoftware.home.api.model.storage.PluginItem;
import com.oberasoftware.home.web.model.WebPluginItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Controller
@RequestMapping("/web/admin/devices")
public class DevicesAdminController {
    private static final Logger LOG = getLogger(DevicesAdminController.class);

    @Autowired
    private ItemManager itemManager;

    @Autowired
    private UIManager uiManager;

    @Autowired
    private ExtensionManager extensionManager;


    @RequestMapping(value = "/{controllerId}")
    public String getPlugins(@PathVariable String controllerId, Model model) {
        List<ControllerItem> controllers = itemManager.findControllers();
        List<PluginItem> plugins = itemManager.findPlugins(controllerId);
        List<WebPluginItem> webPluginItems = plugins.stream()
                .map(p -> new WebPluginItem(p, itemManager.findDevices(controllerId, p.getPluginId())))
                .collect(Collectors.toList());
        List<Container> containers = uiManager.getAllContainers();

        LOG.debug("Retrieving plugins for controller: {} found plugins: {}", controllerId, plugins.size());

        model.addAttribute("controllers", controllers);
        model.addAttribute("plugins", webPluginItems);
        model.addAttribute("selectedController", controllerId);
        model.addAttribute("containers", containers);

        return "admin/devices";
    }

    @RequestMapping
    public String getControllers(Model model) {
        LOG.debug("Showing admin screen - controllers");
        List<ControllerItem> controllers = itemManager.findControllers();
        model.addAttribute("controllers", controllers);

        return "admin/devices";
    }

    @RequestMapping(value = "/icon/plugin/{pluginId}")
    public @ResponseBody ResponseEntity<byte[]> getIcon(@PathVariable String pluginId) throws IOException {
        LOG.debug("Requesting icon for plugin: {}", pluginId);

        AutomationExtension extension = extensionManager.getExtension(pluginId).get();
        Optional<ExtensionResource> optionalResource = extension.getIcon();
        if(optionalResource.isPresent()) {
            ExtensionResource resource = optionalResource.get();
            byte[] data = ByteStreams.toByteArray(resource.getStream());

            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.parseMediaType(resource.getMediaType()));

            return new ResponseEntity<>(data, header, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
