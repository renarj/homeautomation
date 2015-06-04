package com.oberasoftware.home.hue;

import com.oberasoftware.base.event.Event;
import com.philips.lighting.hue.sdk.PHAccessPoint;

/**
 * @author renarj
 */
public class HueBridgeAuthEvent implements Event {
    private final PHAccessPoint ap;

    public HueBridgeAuthEvent(PHAccessPoint ap) {
        this.ap = ap;
    }

    public PHAccessPoint getAp() {
        return ap;
    }

    @Override
    public String toString() {
        return "HueBridgeAuthEvent{" +
                "ap=" + ap.getIpAddress() +
                '}';
    }
}
