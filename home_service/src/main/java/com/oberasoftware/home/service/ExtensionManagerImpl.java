package com.oberasoftware.home.service;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.api.exceptions.RuntimeHomeAutomationException;
import com.oberasoftware.home.api.extensions.AutomationExtension;
import com.oberasoftware.home.api.extensions.DeviceExtension;
import com.oberasoftware.home.api.extensions.ExtensionManager;
import com.oberasoftware.home.api.managers.DeviceManager;
import com.oberasoftware.home.api.managers.ItemManager;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.storage.HomeDAO;
import com.oberasoftware.home.api.storage.model.PluginItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class ExtensionManagerImpl implements ExtensionManager {
    private static final Logger LOG = getLogger(ExtensionManagerImpl.class);

    private ConcurrentMap<String, AutomationExtension> extensions = new ConcurrentHashMap<>();

    @Autowired
    private DeviceManager deviceManager;

    @Autowired
    private HomeDAO homeDAO;

    @Autowired
    private ItemManager itemManager;

    @Autowired
    private AutomationBus automationBus;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public List<AutomationExtension> getExtensions() {
        return new ArrayList<>(extensions.values());
    }

    @Override
    public Optional<AutomationExtension> getExtension(String extensionId) {
        return Optional.ofNullable(extensions.get(extensionId));
    }

    @Override
    public void activateController(String controllerId) throws HomeAutomationException {
        itemManager.createOrUpdateController(controllerId);
    }

    @Override
    public void activateExtension(AutomationExtension extension) throws HomeAutomationException {
        executorService.submit(() -> {
            Optional<PluginItem> pluginItem = homeDAO.findPlugin(automationBus.getControllerId(), extension.getId());
            LOG.info("Activating plugin: {}", pluginItem);
            extension.activate(pluginItem);

            while (!extension.isReady()) {
                LOG.debug("Extension: {} is not ready yet", extension.getId());
                sleepUninterruptibly(1, TimeUnit.SECONDS);
            }

            extensions.putIfAbsent(extension.getId(), extension);

            LOG.info("Registering extension: {}", extension);
            itemManager.createOrUpdatePlugin(automationBus.getControllerId(), extension.getId(), extension.getName(), extension.getProperties());

            if (extension instanceof DeviceExtension) {
                registerDevices((DeviceExtension) extension);
            }

            return null;
        });
    }

    private void registerDevices(DeviceExtension deviceExtension) {
        List<Device> devices = deviceExtension.getDevices();
        devices.forEach(d -> {
            try {
                deviceManager.registerDevice(deviceExtension.getId(), d);
            } catch (HomeAutomationException e) {
                throw new RuntimeHomeAutomationException("Unable to store plugin devices", e);
            }
        });
    }


}
