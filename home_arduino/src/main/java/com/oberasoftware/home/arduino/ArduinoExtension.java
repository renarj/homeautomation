package com.oberasoftware.home.arduino;

import com.google.common.collect.Lists;
import com.oberasoftware.home.api.extensions.CommandHandler;
import com.oberasoftware.home.api.extensions.DeviceExtension;
import com.oberasoftware.home.api.extensions.SpringExtension;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.storage.model.PluginItem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author renarj
 */
@Configuration
@ComponentScan
public class ArduinoExtension implements SpringExtension, DeviceExtension {
    private ApplicationContext context;


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
        assertContext();
        context.getBean(ArduinoConnector.class).connect();
    }

    @Override
    public List<Class<?>> getAnnotatedConfigurationClasses() {
        return Lists.newArrayList(ArduinoExtension.class);
    }

    @Override
    public void provideContext(ApplicationContext context) {
        this.context = context;
    }

    private void assertContext() {
        checkNotNull(context, "ApplicationContext was null, could not load ZWave extension");
    }
}
