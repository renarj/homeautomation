package com.oberasoftware.home.zwave;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oberasoftware.home.api.extensions.CommandHandler;
import com.oberasoftware.home.api.extensions.DeviceExtension;
import com.oberasoftware.home.api.extensions.SpringExtension;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.storage.model.PluginItem;
import com.oberasoftware.home.zwave.exceptions.ZWaveException;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Configuration
@Import(ZWaveConfiguration.class)
@ComponentScan
public class ZWaveSpringExtension implements DeviceExtension, SpringExtension {
    private static final Logger LOG = getLogger(ZWaveSpringExtension.class);

    private ApplicationContext context;

    @Override
    public boolean isReady() {
        assertContext();
        return context.getBean(ZWaveController.class).isNetworkReady();
    }

    @Override
    public List<Device> getDevices() {
        assertContext();
        return context.getBean(DeviceRegistry.class).getDevices();
    }

    @Override
    public void activate(Optional<PluginItem> pluginItem) {
        assertContext();
        try {
            context.getBean(SerialZWaveConnector.class).connect();
            context.getBean(ProtocolBootstrap.class).startInitialization();
        } catch (ZWaveException e) {
            LOG.error("Unable to initialize zwave plugin", e);
        }
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
    public Map<String, String> getProperties() {
        return Maps.newHashMap();
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
