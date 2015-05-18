package com.oberasoftware.home.hue;


import com.oberasoftware.base.event.Event;

/**
 * @author renarj
 */
public class HueBridgeDiscovered implements Event {
    private final String bridgeIp;
    private final String username;

    public HueBridgeDiscovered(String bridgeIp, String username) {
        this.bridgeIp = bridgeIp;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getBridgeIp() {
        return bridgeIp;
    }

    @Override
    public String toString() {
        return "HueBridgeDiscovered{" +
                "bridgeIp='" + bridgeIp + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
