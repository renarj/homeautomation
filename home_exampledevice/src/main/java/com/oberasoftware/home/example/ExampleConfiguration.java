package com.oberasoftware.home.example;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.extensions.CommandHandler;
import com.oberasoftware.home.api.extensions.DeviceExtension;
import com.oberasoftware.home.api.extensions.SpringExtension;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.model.ExtensionResource;
import com.oberasoftware.home.api.model.Status;
import com.oberasoftware.home.api.storage.model.PluginItem;
import com.oberasoftware.home.core.model.ExtensionResourceImpl;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Configuration
@ComponentScan
public class ExampleConfiguration implements DeviceExtension, SpringExtension {
    private static final Logger LOG = getLogger(ExampleConfiguration.class);

    private static final Device exampleDevice1 = new ExampleDevice("1", "Test Device 1", Status.ACTIVE,
            ImmutableMap.<String, String>builder().put("manufacturer", "example").build());
    private static final Device exampleDevice2 = new ExampleDevice("2", "Test Device 4", Status.ACTIVE,
            ImmutableMap.<String, String>builder().put("manufacturer", "notTheSame").put("batteryDevice", "true").build());

    public ExampleConfiguration() {

    }

    @Override
    public List<Device> getDevices() {
        return Lists.newArrayList(exampleDevice1, exampleDevice2);
    }

    @Override
    public String getId() {
        return "example";
    }

    @Override
    public String getName() {
        return "Example Plugin";
    }

    @Override
    public Map<String, String> getProperties() {
        return ImmutableMap.<String, String>builder()
                .put("token", "$&@(*#&@JKJASDLAJSDLJ").build();
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
        InputStream stream = this.getClass().getResourceAsStream("/hue.jpeg");
        if(stream != null) {
            LOG.debug("Loaded test image");
            return Optional.of(new ExtensionResourceImpl("image/jpg", stream));
        }

        return Optional.empty();
    }

    @Override
    public void activate(Optional<PluginItem> pluginItem) {

    }

    @Override
    public List<Class<?>> getAnnotatedConfigurationClasses() {
        return Lists.newArrayList(ExampleConfiguration.class);
    }

    @Override
    public void provideContext(ApplicationContext applicationContext) {

    }
}
