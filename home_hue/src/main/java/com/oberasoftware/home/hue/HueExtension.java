package com.oberasoftware.home.hue;

import com.google.common.collect.Lists;
import com.oberasoftware.home.api.extensions.CommandHandler;
import com.oberasoftware.home.api.extensions.DeviceExtension;
import com.oberasoftware.home.api.extensions.SpringExtension;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.storage.model.DevicePlugin;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author renarj
 */
@Configuration
@ComponentScan
public class HueExtension implements DeviceExtension, SpringExtension {

    private ApplicationContext context;

    @Override
    public String getId() {
        return "hue";
    }

    @Override
    public String getName() {
        return "Philips Hue plugin";
    }

    @Override
    public boolean isDeviceReady() {
        assertContext();
        return context.getBean(HueConnector.class).isConnected();
    }

    @Override
    public List<Device> getDevices() {
        return null;
    }

    @Override
    public void activate(Optional<DevicePlugin> pluginItem) {
        context.getBean(HueConnector.class).connect(pluginItem);
    }

    @Override
    public CommandHandler getCommandHandler() {
        assertContext();
        return null;
    }

    @Override
    public List<Class<?>> getAnnotatedConfigurationClasses() {
        return Lists.newArrayList(HueExtension.class);
    }

    @Override
    public void provideContext(ApplicationContext context) {
        this.context = context;
    }

    private void assertContext() {
        checkNotNull(context, "ApplicationContext was null, could not load ZWave extension");
    }
}
