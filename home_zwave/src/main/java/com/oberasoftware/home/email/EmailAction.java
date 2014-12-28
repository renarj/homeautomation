package com.oberasoftware.home.email;

import com.oberasoftware.home.zwave.api.Action;

/**
 * @author renarj
 */
public class EmailAction implements Action {

    private final int nodeId;
    private final String message;
    private final String to;

    public EmailAction(int nodeId, String to, String message) {
        this.nodeId = nodeId;
        this.to = to;
        this.message = message;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "EmailAction{" +
                "to='" + to + '\'' +
                ", message='" + message + '\'' +
                ", nodeId=" + nodeId +
                '}';
    }
}
