package com.oberasoftware.home.zwave.api.events;

/**
 * @author renarj
 */
public class SendDataStateEvent implements ControllerEvent {
    private final SEND_STATE state;

    public SendDataStateEvent(SEND_STATE state) {
        this.state = state;
    }

    public SEND_STATE getState() {
        return state;
    }

    @Override
    public String toString() {
        return "SendDataStateEvent{" +
                "state=" + state +
                '}';
    }
}
