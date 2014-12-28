package com.oberasoftware.home.api.extensions;

import java.util.List;

/**
 * @author renarj
 */
public interface SpringExtension {
    List<Class<?>> getAnnotatedConfigurationClasses();
}
