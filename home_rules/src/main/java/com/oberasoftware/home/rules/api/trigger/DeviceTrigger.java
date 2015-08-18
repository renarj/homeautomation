package com.oberasoftware.home.rules.api.trigger;

/**
 * @author Renze de Vries
 */
public class DeviceTrigger implements Trigger {
    public enum TRIGGER_TYPE {
        DEVICE_STATE_CHANGE,
        DEVICE_EVENT
    }

    private TRIGGER_TYPE triggerType;

    public DeviceTrigger(TRIGGER_TYPE triggerType) {
        this.triggerType = triggerType;
    }

    public DeviceTrigger() {
    }

    public TRIGGER_TYPE getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(TRIGGER_TYPE triggerType) {
        this.triggerType = triggerType;
    }

    @Override
    public String toString() {
        return "DeviceTrigger{" +
                "triggerType=" + triggerType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceTrigger that = (DeviceTrigger) o;

        return triggerType == that.triggerType;

    }

    @Override
    public int hashCode() {
        return triggerType.hashCode();
    }
}
