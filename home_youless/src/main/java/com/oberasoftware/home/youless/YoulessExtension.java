package com.oberasoftware.home.youless;

import com.google.common.collect.Lists;
import com.oberasoftware.home.api.extensions.CommandHandler;
import com.oberasoftware.home.api.extensions.DeviceExtension;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.storage.model.PluginItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Renze de Vries
 */
@Component
public class YoulessExtension implements DeviceExtension {

    @Autowired
    private YoulessConnector connector;

    @Override
    public List<Device> getDevices() {
        YoulessDevice device = new YoulessDevice(connector.getYoulessIp(), "Youless");

        return Lists.newArrayList(device);
    }

    @Override
    public String getId() {
        return "youless";
    }

    @Override
    public String getName() {
        return "Youless Plugin";
    }

    @Override
    public Map<String, String> getProperties() {
        return new HashMap<>();
    }

    @Override
    public CommandHandler getCommandHandler() {
        return null;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void activate(Optional<PluginItem> pluginItem) {
        connector.connect();
    }
}
