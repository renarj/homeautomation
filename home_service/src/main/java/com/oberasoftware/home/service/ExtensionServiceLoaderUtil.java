package com.oberasoftware.home.service;

import com.oberasoftware.home.api.extensions.SpringExtension;
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

    private final List<SpringExtension> springExtensions = new ArrayList<>();

    private ExtensionServiceLoaderUtil() {
        ServiceLoader<SpringExtension> extensions = ServiceLoader.load(SpringExtension.class);
        extensions.forEach(e -> {
            LOG.debug("Found spring extension: {}", e);
            springExtensions.add(e);
        });
    }

    public static List<SpringExtension> getExtensions() {
        return INSTANCE.springExtensions;
    }
}
