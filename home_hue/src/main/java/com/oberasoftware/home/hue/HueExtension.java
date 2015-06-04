package com.oberasoftware.home.hue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oberasoftware.home.api.extensions.CommandHandler;
import com.oberasoftware.home.api.extensions.DeviceExtension;
import com.oberasoftware.home.api.extensions.SpringExtension;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.storage.model.PluginItem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author renarj
 */
@Configuration
@ComponentScan
public class HueExtension implements DeviceExtension, SpringExtension {

    private ApplicationContext context;

    public static final String HUE_ID = "hue";
    public static final String HUE_NAME = "Philips Hue plugin";

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
        assertContext();
        return context.getBean(HueConnector.class).isConnected();
    }

    @Override
    public List<Device> getDevices() {
        assertContext();
        return context.getBean(HueDeviceManager.class).getDevices();
    }

    @Override
    public void activate(Optional<PluginItem> pluginItem) {
        context.getBean(HueConnector.class).connect(pluginItem);
    }

    @Override
    public CommandHandler getCommandHandler() {
        assertContext();
        return context.getBean(HueCommandHandler.class);
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
