package com.oberasoftware.home.zwave;

import com.oberasoftware.home.MessageTopic;
import com.oberasoftware.home.TopicListener;
import com.oberasoftware.home.zwave.connector.ControllerConnector;
import com.oberasoftware.home.zwave.exceptions.ZWaveException;
import com.oberasoftware.home.zwave.messages.ZWaveMessage;

/**
 * @author renarj
 */
public interface Controller {
    void connect(ControllerConnector connector) throws ZWaveException;

    void disconnect() throws ZWaveException;

    MessageTopic<ZWaveMessage> getMessageQueue() throws ZWaveException;

    void subscribe(TopicListener<ZWaveMessage> topicListener);

    void send(ZWaveMessage message) throws ZWaveException;
}
