package com.oberasoftware.home.core;

import com.oberasoftware.home.api.extensions.AutomationExtension;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
public class ExtensionServiceLoaderUtil {
    private static final Logger LOG = getLogger(ExtensionServiceLoaderUtil.class);

    private static final ExtensionServiceLoaderUtil INSTANCE = new ExtensionServiceLoaderUtil();

    private final List<AutomationExtension> automationExtensions = new ArrayList<>();

    private ExtensionServiceLoaderUtil() {
        ServiceLoader<AutomationExtension> extensions = ServiceLoader.load(AutomationExtension.class);
        extensions.forEach(e -> {
            LOG.debug("Found extension: {}", e);
            automationExtensions.add(e);
        });
    }

    public static List<AutomationExtension> getExtensions() {
        return INSTANCE.automationExtensions;
    }
}
