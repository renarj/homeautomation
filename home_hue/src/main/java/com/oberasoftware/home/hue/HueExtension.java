package com.oberasoftware.home.hue;

import com.google.common.collect.Maps;
import com.oberasoftware.home.api.commands.handlers.CommandHandler;
import com.oberasoftware.home.api.extensions.DeviceExtension;
import com.oberasoftware.home.api.extensions.ExtensionCapability;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.model.storage.PluginItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Renze de Vries
 */
@Component
public class HueExtension implements DeviceExtension {

    public static final String HUE_ID = "hue";
    public static final String HUE_NAME = "Philips Hue plugin";

    @Autowired
    private HueConnector hueConnector;

    @Autowired
    private HueCommandHandler hueCommandHandler;

    @Autowired
    private HueDeviceManager hueDeviceManager;

    @Override
    public boolean supports(ExtensionCapability capability) {
        return capability == ExtensionCapability.GroupSupport;
    }

    @Override
    public String getId() {
        return HUE_ID;
    }

    @Override
    public String getName() {
        return HUE_NAME;
    }

    @Override
    public Map<String, String> getProperties() {
        return Maps.newHashMap();
    }

    @Override
    public boolean isReady() {
        return hueConnector.isConnected();
    }

    @Override
    public List<Device> getDevices() {
        return hueDeviceManager.getDevices();
    }

    @Override
    public void activate(Optional<PluginItem> pluginItem) {
        hueConnector.connect(pluginItem);
    }

    @Override
    public CommandHandler getCommandHandler() {
        return hueCommandHandler;
    }
}
