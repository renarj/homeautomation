package com.oberasoftware.home.api.extensions;

import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * @author renarj
 */
public interface SpringExtension {
    List<Class<?>> getAnnotatedConfigurationClasses();

    void provideContext(ApplicationContext applicationContext);
}
