package com.oberasoftware.home.api.extensions;

import com.oberasoftware.home.api.exceptions.DataStoreException;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;

import java.util.List;

/**
 * @author renarj
 */
public interface ExtensionManager {
    void registerController(String controllerId) throws DataStoreException;

    void registerExtension(AutomationExtension extension) throws HomeAutomationException;

    List<AutomationExtension> getExtensions();
}
