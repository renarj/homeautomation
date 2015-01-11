package com.oberasoftware.home.service;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.api.exceptions.RuntimeHomeAutomationException;
import com.oberasoftware.home.api.extensions.AutomationExtension;
import com.oberasoftware.home.api.extensions.ExtensionManager;
import com.oberasoftware.home.api.extensions.SpringExtension;
import com.oberasoftware.home.rest.RestConfiguration;
import com.oberasoftware.home.storage.jasdb.JasDBConfiguration;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class})
@Import({RestConfiguration.class, JasDBConfiguration.class})
@ComponentScan
public class HomeAutomation {
    private static final Logger LOG = getLogger(HomeAutomation.class);

    public HomeAutomation() {

    }

    public void start(String[] args) {
        LOG.info("Starting HomeAutomation system");

        try {
            List<AutomationExtension> automationExtensions = ExtensionServiceLoaderUtil.getExtensions();
            List<SpringExtension> springExtensions = automationExtensions.stream().filter(e -> e instanceof SpringExtension).map(e -> (SpringExtension) e).collect(Collectors.toList());

            List<Class<?>> c = springExtensions.stream().flatMap(s -> s.getAnnotatedConfigurationClasses().stream()).collect(Collectors.toList());
            c.add(HomeAutomation.class);

            LOG.debug("Starting spring context with configuration classes: {}", c);
            ApplicationContext context = SpringApplication.run(c.toArray(), args);

            String controllerId = context.getBean(AutomationBus.class).getControllerId();

            springExtensions.forEach(s -> s.provideContext(context));

            ExtensionManager extensionManager = context.getBean(ExtensionManager.class);
            extensionManager.registerController(controllerId);
            automationExtensions.forEach(x -> {
                try {
                    extensionManager.registerExtension(x);
                } catch (HomeAutomationException e) {
                    throw new RuntimeHomeAutomationException("Unable to register home automation extension", e);
                }
            });
        } catch (HomeAutomationException | RuntimeHomeAutomationException e) {
            LOG.error("Could not start the HomeAutomationSystem", e);
        }
    }

    public static void main(String[] args) {
        HomeAutomation automation = new HomeAutomation();
        automation.start(args);
    }

}
