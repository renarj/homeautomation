package com.oberasoftware.home.hue;

import com.oberasoftware.home.api.model.storage.PluginItem;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;

import java.util.Optional;

/**
 * @author renarj
 */
public interface HueConnector {
    void connect(Optional<PluginItem> pluginItem);

    PHHueSDK getSdk();

    PHBridge getBridge();

    boolean isConnected();
}
