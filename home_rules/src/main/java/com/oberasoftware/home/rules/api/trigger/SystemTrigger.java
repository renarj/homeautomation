package com.oberasoftware.home.rules.api.trigger;

/**
 * @author Renze de Vries
 */
public class SystemTrigger implements Trigger {

    public boolean equals(Object o) {
        return this == o || o != null && o.getClass() == getClass();
    }

    @Override
    public String toString() {
        return "SystemTrigger{}";
    }
}
