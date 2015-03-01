package com.oberasoftware.home.example;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.oberasoftware.home.api.extensions.CommandHandler;
import com.oberasoftware.home.api.extensions.DeviceExtension;
import com.oberasoftware.home.api.extensions.SpringExtension;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.model.Status;
import com.oberasoftware.home.api.storage.model.PluginItem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author renarj
 */
@Configuration
@ComponentScan
public class ExampleConfiguration implements DeviceExtension, SpringExtension {

    private static final Device exampleDevice1 = new ExampleDevice("1", "Test Device 1", Status.ACTIVE,
            ImmutableMap.<String, String>builder().put("manufacturer", "example").build());
    private static final Device exampleDevice2 = new ExampleDevice("2", "Test Device 4", Status.ACTIVE,
            ImmutableMap.<String, String>builder().put("manufacturer", "notTheSame").put("batteryDevice", "true").build());


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
        return null;
    }

    @Override
    public boolean isReady() {
        return true;
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
