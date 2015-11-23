package com.oberasoftware.home.zwave;

import com.google.common.collect.Maps;
import com.oberasoftware.home.api.commands.handlers.CommandHandler;
import com.oberasoftware.home.api.extensions.DeviceExtension;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.model.storage.PluginItem;
import com.oberasoftware.home.zwave.exceptions.ZWaveConfigurationException;
import com.oberasoftware.home.zwave.exceptions.ZWaveException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class ZWaveSpringExtension implements DeviceExtension {
    private static final Logger LOG = getLogger(ZWaveSpringExtension.class);

    @Autowired
    private ZWaveController zWaveController;

    @Autowired
    private DeviceRegistry deviceRegistry;

    @Autowired
    private SerialZWaveConnector serialZWaveConnector;

    @Autowired
    private ZWaveCommandHandler commandHandler;

    @Override
    public boolean isReady() {
        return zWaveController.isNetworkReady();
    }

    @Override
    public List<Device> getDevices() {
        return deviceRegistry.getDevices();
    }

    @Override
    public void activate(Optional<PluginItem> pluginItem) {
        try {
            serialZWaveConnector.connect();
            zWaveController.initializeNetwork();
        } catch(ZWaveConfigurationException e) {
            LOG.warn("ZWave was not correctly configured: {}", e.getMessage());
        } catch (ZWaveException e) {
            LOG.error("Unable to initialize zwave plugin", e);
        }
    }

    @Override
    public String getId() {
        return deviceRegistry.getZwaveId();
    }

    @Override
    public String getName() {
        return deviceRegistry.getZwaveName();
    }

    @Override
    public Map<String, String> getProperties() {
        return Maps.newHashMap();
    }

    @Override
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    @Override
    public String toString() {
        return "ZWaveSpringExtension{" + "name=" + getName() + ",id=" + getId() + "}";
    }
}
