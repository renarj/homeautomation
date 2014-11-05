package com.oberasoftware.home.zwave.connector;

import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.zwave.exceptions.ZWaveException;
import com.oberasoftware.home.zwave.messages.ZWaveMessage;
import com.oberasoftware.home.zwave.messages.ZWaveRawMessage;

/**
 * @author renarj
 */
public interface ControllerConnector {
    void connect() throws ZWaveException;

    void close() throws ZWaveException;

    void subscribe(EventListener<ZWaveMessage> zWaveMessageTopicListener);

    void send(ZWaveRawMessage rawMessage);

    boolean isConnected();
}
