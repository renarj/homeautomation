package com.oberasoftware.home.zwave;

import com.google.common.collect.Lists;
import com.oberasoftware.home.api.extensions.CommandHandler;
import com.oberasoftware.home.api.extensions.DeviceExtension;
import com.oberasoftware.home.api.extensions.SpringExtension;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.storage.model.DevicePlugin;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author renarj
 */
@Configuration
@Import(ZWaveConfiguration.class)
@ComponentScan
public class ZWaveSpringExtension implements DeviceExtension, SpringExtension {

    private ApplicationContext context;

    @Override
    public boolean isDeviceReady() {
        assertContext();
        return context.getBean(ZWaveController.class).isNetworkReady();
    }

    @Override
    public List<Device> getDevices() {
        assertContext();
        return context.getBean(DeviceRegistry.class).getDevices();
    }

    @Override
    public void activate(Optional<DevicePlugin> pluginItem) {
        assertContext();
        context.getBean(ProtocolBootstrap.class).startInitialization();
    }

    @Override
    public String getId() {
        assertContext();
        return context.getBean(DeviceRegistry.class).getZwaveId();
    }

    @Override
    public String getName() {
        assertContext();
        return context.getBean(DeviceRegistry.class).getZwaveName();
    }

    @Override
    public CommandHandler getCommandHandler() {
        assertContext();
        return context.getBean(ZWaveCommandHandler.class);
    }

    @Override
    public List<Class<?>> getAnnotatedConfigurationClasses() {
        return Lists.newArrayList(ZWaveConfiguration.class);
    }

    @Override
    public void provideContext(ApplicationContext context) {
        this.context = context;
    }

    private void assertContext() {
        checkNotNull(context, "ApplicationContext was null, could not load ZWave extension");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ZWaveSpringExtension{");
        if(context != null) {
            builder.append("name=").append(getName());
            builder.append(",id=").append(getId());
        } else {
            builder.append("Not Initialized");
        }
        builder.append("}");

        return builder.toString();
    }
}
