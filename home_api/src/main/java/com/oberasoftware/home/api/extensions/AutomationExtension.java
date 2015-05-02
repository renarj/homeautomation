package com.oberasoftware.home.api.extensions;

import com.oberasoftware.home.api.model.ExtensionResource;
import com.oberasoftware.home.api.storage.model.PluginItem;

import java.util.Map;
import java.util.Optional;

/**
 * @author renarj
 */
public interface AutomationExtension {
    String getId();

    String getName();

    Map<String, String> getProperties();

    CommandHandler getCommandHandler();

    default Optional<ExtensionResource> getIcon() {
        return Optional.empty();
    }

    boolean isReady();

    void activate(Optional<PluginItem> pluginItem);
}
