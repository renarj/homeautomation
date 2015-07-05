package com.oberasoftware.home.nest;

import com.oberasoftware.home.api.commands.handlers.CommandHandler;
import com.oberasoftware.home.api.extensions.DeviceExtension;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.model.storage.PluginItem;
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
public class NestExtension implements DeviceExtension {

    @Autowired
    private ThermostatManager thermostatManager;

    @Autowired
    private NestConnector nestConnector;

    @Override
    public List<Device> getDevices() {
        return thermostatManager.getDevices();
    }

    @Override
    public String getId() {
        return "nest";
    }

    @Override
    public String getName() {
        return "Nest Plugin";
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
        return nestConnector.isConnected();
    }

    @Override
    public void activate(Optional<PluginItem> pluginItem) {
        nestConnector.activate();
    }
}
