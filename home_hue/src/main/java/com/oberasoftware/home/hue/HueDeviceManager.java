package com.oberasoftware.home.hue;

import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.api.model.Status;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class HueDeviceManager {
    private static final Logger LOG = getLogger(HueDeviceManager.class);

    @Autowired
    private HueConnector hueConnector;

    public List<Device> getDevices() {
        PHHueSDK sdk = hueConnector.getSdk();

        sdk.getSelectedBridge().getResourceCache().getAllLights().forEach(l -> l.getName());

        PHBridge bridge = sdk.getSelectedBridge();
        if(bridge != null) {
            return bridge.getResourceCache().getAllLights().stream()
                    .map(l -> {
                        String lightId = l.getIdentifier();
                        String name = l.getName();

                        Map<String, String> properties = new HashMap<>();
                        properties.put("modelNumber", l.getModelNumber());
                        properties.put("version", l.getVersionNumber());
                        properties.put("lightType", l.getLightType().name());

                        LOG.debug("Found a Hue Light id: {} with name: {}", l.getIdentifier(), l.getName());

                        return new HueDevice(lightId, name, Status.DISCOVERED, properties);
                    }).collect(Collectors.toList());
        } else {
            LOG.debug("No Hue bridge was connected");
        }

        return new ArrayList<>();
    }
}
