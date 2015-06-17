package com.oberasoftware.home.api.extensions;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;

import java.util.List;
import java.util.Optional;

/**
 * @author renarj
 */
public interface ExtensionManager {
    void activateController(String controllerId) throws HomeAutomationException;

    void activateExtensions() throws HomeAutomationException;

    List<AutomationExtension> getExtensions();

    Optional<AutomationExtension> getExtension(String extensionId);
}
