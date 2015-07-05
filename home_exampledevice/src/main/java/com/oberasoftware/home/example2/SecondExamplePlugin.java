package com.oberasoftware.home.example2;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.events.devices.DeviceNumericValueEvent;
import com.oberasoftware.home.api.events.devices.OnOffValueEvent;
import com.oberasoftware.home.api.commands.handlers.CommandHandler;
import com.oberasoftware.home.api.extensions.DeviceExtension;
import com.oberasoftware.home.api.extensions.SpringExtension;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.model.ExtensionResource;
import com.oberasoftware.home.api.model.Status;
import com.oberasoftware.home.api.model.storage.PluginItem;
import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.core.model.ExtensionResourceImpl;
import com.oberasoftware.home.core.types.ValueImpl;
import com.oberasoftware.home.example.ExampleCommandHandler;
import com.oberasoftware.home.example.ExampleDevice;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
@ComponentScan
public class SecondExamplePlugin implements DeviceExtension, SpringExtension {
    private static final Logger LOG = getLogger(SecondExamplePlugin.class);

    private static final Device exampleDevice1 = new ExampleDevice("1", "Switchable Device", Status.ACTIVE,
            ImmutableMap.<String, String>builder().put("manufacturer", "me :P").build());
    private static final Device exampleDevice2 = new ExampleDevice("2", "Temperature Device", Status.ACTIVE,
            ImmutableMap.<String, String>builder().put("manufacturer", "pietje").put("batteryDevice", "true").build());

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    private AutomationBus automationBus;

    @Override
    public List<Device> getDevices() {
        return Lists.newArrayList(exampleDevice1, exampleDevice2);
    }

    @Override
    public String getId() {
        return "Example2";
    }

    @Override
    public String getName() {
        return "Another Plugin";
    }

    @Override
    public Map<String, String> getProperties() {
        return ImmutableMap.<String, String>builder()
                .put("ip", "10.1.0.120")
                .put("user", "guest")
                .put("password", "guest").build();
    }

    @Override
    public CommandHandler getCommandHandler() {
        return new ExampleCommandHandler();
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public Optional<ExtensionResource> getIcon() {
        InputStream stream = this.getClass().getResourceAsStream("/zwave.png");
        if(stream != null) {
            LOG.debug("Loaded test image");
            return Optional.of(new ExtensionResourceImpl("image/png", stream));
        }

        return Optional.empty();
    }

    @Override
    public void activate(Optional<PluginItem> pluginItem) {
        Random rnd = new Random();
        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(() -> automationBus.publish(
                        new DeviceNumericValueEvent(automationBus.getControllerId(), getId(), "2", new ValueImpl(VALUE_TYPE.NUMBER, rnd.nextInt(35)), "temperature")),
                0l, 1l, TimeUnit.MINUTES);

        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(() -> automationBus.publish(
                new OnOffValueEvent(automationBus.getControllerId(), getId(), "1", rnd.nextBoolean())), 0l, 1l, TimeUnit.MINUTES);
    }
}
