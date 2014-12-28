package com.oberasoftware.home.api.extensions;

import com.oberasoftware.home.api.model.Capability;

import java.util.List;

/**
 * @author renarj
 */
public interface CapabilityExtension extends AutomationExtension {
    List<Capability> getCapabilities();
}
