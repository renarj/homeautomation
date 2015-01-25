package com.oberasoftware.home.youless;

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
public class YoulessExtension implements DeviceExtension, SpringExtension {

    private ApplicationContext applicationContext;


    @Override
    public List<Device> getDevices() {
        assertContext();

        YoulessConnector connector = applicationContext.getBean(YoulessConnector.class);
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
        assertContext();

        applicationContext.getBean(YoulessConnector.class).connect();
    }

    @Override
    public List<Class<?>> getAnnotatedConfigurationClasses() {
        return Lists.newArrayList(YoulessExtension.class);
    }

    @Override
    public void provideContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private void assertContext() {
        checkNotNull(applicationContext, "ApplicationContext was null, could not load ZWave extension");
    }

}
