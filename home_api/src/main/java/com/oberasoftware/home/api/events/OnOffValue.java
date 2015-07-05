package com.oberasoftware.home.api.events;

import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.api.types.Value;

/**
 * @author Renze de Vries
 */
public class OnOffValue implements Value {

    public static String LABEL = "on-off";

    private final boolean on;

    public OnOffValue(boolean on) {
        this.on = on;
    }

    public boolean isOn() {
        return on;
    }

    @Override
    public VALUE_TYPE getType() {
        return VALUE_TYPE.STRING;
    }

    @Override
    public String getValue() {
        return asString();
    }

    @Override
    public String asString() {
        return isOn() ? "on" : "off";
    }

    @Override
    public String toString() {
        return "OnOffValue{" +
                "on=" + on +
                '}';
    }
}
