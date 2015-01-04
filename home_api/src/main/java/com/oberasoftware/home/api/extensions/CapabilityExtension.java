package com.oberasoftware.home.api.extensions;

import com.oberasoftware.home.api.storage.model.Capability;

import java.util.List;

/**
 * @author renarj
 */
public interface CapabilityExtension extends AutomationExtension {
    List<Capability> getCapabilities();
}
