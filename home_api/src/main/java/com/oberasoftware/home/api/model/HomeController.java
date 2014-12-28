package com.oberasoftware.home.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author renarj
 */
public class HomeController {
    private final List<Capability> supportedCapabilities = new ArrayList<>();

    private final String controllerId;

    public HomeController(String controllerId) {
        this.controllerId = controllerId;
    }

    public List<Capability> getSupportedCapabilities() {
        return supportedCapabilities;
    }

    public String getControllerId() {
        return controllerId;
    }
}
