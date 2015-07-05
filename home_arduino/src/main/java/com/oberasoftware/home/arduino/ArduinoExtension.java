package com.oberasoftware.home.arduino;

import com.oberasoftware.home.api.commands.handlers.CommandHandler;
import com.oberasoftware.home.api.extensions.DeviceExtension;
import com.oberasoftware.home.api.extensions.SpringExtension;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.model.storage.PluginItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author renarj
 */
@Configuration
@ComponentScan
public class ArduinoExtension implements SpringExtension, DeviceExtension {
    @Autowired
    private ArduinoConnector arduinoConnector;

    @Override
    public List<Device> getDevices() {
        return new ArrayList<>();
    }

    @Override
    public String getId() {
        return "arduino";
    }

    @Override
    public String getName() {
        return "Arduino connector";
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
        arduinoConnector.connect();
    }
}
