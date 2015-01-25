package com.oberasoftware.home.nest;

import com.google.common.collect.Lists;
import com.oberasoftware.home.api.extensions.CommandHandler;
import com.oberasoftware.home.api.extensions.DeviceExtension;
import com.oberasoftware.home.api.extensions.SpringExtension;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.storage.model.PluginItem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
public class NestExtension implements DeviceExtension, SpringExtension {

    private ApplicationContext applicationContext;

    @Override
    public List<Device> getDevices() {
        assertContext();
        return applicationContext.getBean(TheromostatManager.class).getDevices();
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
        return applicationContext.getBean(NestConnector.class).isConnected();
    }

    @Override
    public void activate(Optional<PluginItem> pluginItem) {
        assertContext();
        applicationContext.getBean(NestConnector.class).activate();
    }

    @Override
    public List<Class<?>> getAnnotatedConfigurationClasses() {
        return Lists.newArrayList(NestExtension.class);
    }

    @Override
    public void provideContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private void assertContext() {
        checkNotNull(applicationContext, "ApplicationContext was null, could not load ZWave extension");
    }
}
