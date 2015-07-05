package com.oberasoftware.home.api.extensions;

import com.oberasoftware.home.api.commands.handlers.CommandHandler;
import com.oberasoftware.home.api.model.ExtensionResource;
import com.oberasoftware.home.api.model.storage.PluginItem;

import java.util.Map;
import java.util.Optional;

/**
 * @author renarj
 */
public interface AutomationExtension {
    String getId();

    String getName();

    default boolean supports(ExtensionCapability capability) {
        return false;
    }

    Map<String, String> getProperties();

    CommandHandler getCommandHandler();

    default Optional<ExtensionResource> getIcon() {
        return Optional.empty();
    }

    boolean isReady();

    void activate(Optional<PluginItem> pluginItem);
}
