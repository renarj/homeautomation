package com.oberasoftware.home.hue;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.events.controller.PluginUpdateEvent;
import com.oberasoftware.home.api.storage.model.PluginItem;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueParsingError;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class HueConnectorImpl implements EventHandler, HueConnector {
    private static final Logger LOG = getLogger(HueConnectorImpl.class);

    private PHHueSDK sdk;
    private PHAccessPoint ap;

    private String bridgeUser;
    private String bridgeIp;

    private AtomicBoolean connected = new AtomicBoolean(false);

    @Autowired
    private AutomationBus automationBus;

    @Override
    public void connect(Optional<PluginItem> pluginItem) {
        LOG.info("Connecting to Philips HUE bridge");

        sdk = PHHueSDK.create();
        sdk.setAppName("Home Automation System");

        sdk.getNotificationManager().registerSDKListener(new HueListener());

        if(!pluginItem.isPresent()) {
            LOG.info("No bridge configured");
//            startSearchBrige();
        } else {
            Map<String, String> properties = pluginItem.get().getProperties();
            this.bridgeIp = properties.get("bridgeIp");
            this.bridgeUser = properties.get("username");
//            if(bridgeIp != null && bridgeUser != null) {
                LOG.info("Existing bridge found: {} username: {}", bridgeIp, bridgeUser);
                automationBus.publish(new HueBridgeDiscovered("10.1.0.249", "883cadb2-d653-4a3e-bfa1-31e43060c15"));
//            } else {
//                startSearchBrige();
//            }

        }
    }

    private void startSearchBrige() {
        LOG.info("No existing bridge found, searching for a bridge");
        PHBridgeSearchManager sm = (PHBridgeSearchManager) sdk.getSDKService(PHHueSDK.SEARCH_BRIDGE);
        sm.search(true, true);
    }

    @Override
    public PHHueSDK getSdk() {
        return sdk;
    }

    @Override
    public PHBridge getBridge() {
        return sdk.getSelectedBridge();
    }

    private class HueListener implements PHSDKListener {
        @Override
        public void onCacheUpdated(List<Integer> list, PHBridge phBridge) {
            LOG.debug("Cache updated");
        }

        @Override
        public void onBridgeConnected(PHBridge phBridge) {
            LOG.info("Bridge connected: {} with user: {}", phBridge, bridgeUser);
            connected.set(true);

            sdk.setSelectedBridge(phBridge);
            sdk.enableHeartbeat(phBridge, PHHueSDK.HB_INTERVAL);

            Map<String, String> properties = new HashMap<>();
            properties.put("bridgeIp", bridgeIp);
            properties.put("username", bridgeUser);

            automationBus.publish(new PluginUpdateEvent(HueExtension.HUE_ID, HueExtension.HUE_NAME, properties));
        }

        @Override
        public void onAuthenticationRequired(PHAccessPoint phAccessPoint) {
            LOG.info("Hue authentication required: {}", phAccessPoint.getIpAddress());
            automationBus.publish(new HueBridgeAuthEvent(phAccessPoint));
        }

        @Override
        public void onAccessPointsFound(List<PHAccessPoint> list) {
            list.forEach(a -> LOG.debug("Found accesspoint: {}", a.getIpAddress()));

            if(list.size() == 1) {
                Optional<PHAccessPoint> ap = list.stream().findFirst();

                bridgeUser = UUID.randomUUID().toString();
                bridgeIp = ap.get().getIpAddress();

                ap.ifPresent(a -> automationBus.publish(new HueBridgeDiscovered(a.getIpAddress(), bridgeUser)));
            } else {
                LOG.warn("Detected multiple accesspoints");
            }
        }

        @Override
        public void onError(int i, String s) {
            LOG.error("Hue Connection error: {} reason: {}", i, s);
        }

        @Override
        public void onConnectionResumed(PHBridge phBridge) {
            LOG.trace("Connection resumed");
        }

        @Override
        public void onConnectionLost(PHAccessPoint phAccessPoint) {
            LOG.debug("Connection lost");
        }

        @Override
        public void onParsingErrors(List<PHHueParsingError> list) {
            LOG.debug("Parsing error");
        }
    }

    @EventSubscribe
    public void receive(HueBridgeDiscovered bridgeEvent) {
        LOG.info("Connecting to bridge: {} with user: {}", bridgeEvent.getBridgeIp(), bridgeEvent.getUsername());

        ap = new PHAccessPoint();
        ap.setIpAddress(bridgeEvent.getBridgeIp());
        ap.setUsername(bridgeEvent.getUsername());

        sdk.connect(ap);
    }

    @EventSubscribe
    public void receive(HueBridgeAuthEvent authEvent) {
        LOG.info("Authentication on bridge required: {}", authEvent);

        sdk.startPushlinkAuthentication(authEvent.getAp());
        LOG.info("Please push the link button on your Philips Hue Bridge: {}", authEvent.getAp().getIpAddress());
    }

    @Override
    public boolean isConnected() {
        return connected.get();
    }

    public void setAutomationBus(AutomationBus automationBus) {
        this.automationBus = automationBus;
    }
}
