package com.oberasoftware.home.hue;

import com.oberasoftware.home.api.storage.model.PluginItem;
import com.philips.lighting.hue.sdk.PHHueSDK;

import java.util.Optional;

/**
 * @author renarj
 */
public interface HueConnector {
    void connect(Optional<PluginItem> pluginItem);

    PHHueSDK getSdk();

    boolean isConnected();
}
