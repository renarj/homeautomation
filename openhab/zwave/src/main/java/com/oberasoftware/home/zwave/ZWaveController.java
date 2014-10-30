package com.oberasoftware.home.zwave;

import com.oberasoftware.home.MessageTopic;
import com.oberasoftware.home.TopicListener;
import com.oberasoftware.home.zwave.connector.ControllerConnector;
import com.oberasoftware.home.zwave.exceptions.ZWaveException;
import com.oberasoftware.home.zwave.messages.ZWaveMessage;
import com.oberasoftware.home.zwave.messages.ZWaveRawMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author renarj
 */
public class ZWaveController implements Controller, TopicListener<ZWaveMessage> {
    private static final Logger LOG = LoggerFactory.getLogger(ZWaveController.class);

    private ControllerConnector connector;

    public ZWaveController() {

    }

    @Override
    public void connect(ControllerConnector connector) throws ZWaveException {
        this.connector = connector;

        LOG.info("Connecting to controller with connector: {}", connector);
        connector.connect();
        connector.subscribe(this);
    }

    @Override
    public void disconnect() throws ZWaveException {
        if(this.connector != null) {
            LOG.info("Disconnecting ZWave controller connector: {}", this.connector);
            this.connector.close();
        }
    }

    @Override
    public MessageTopic<ZWaveMessage> getMessageQueue() throws ZWaveException {
        return null;
    }

    @Override
    public void subscribe(TopicListener<ZWaveMessage> topicListener) {
        connector.subscribe(topicListener);
    }

    @Override
    public void send(ZWaveMessage message) throws ZWaveException {
        connector.send((ZWaveRawMessage)message);
    }

    @Override
    public void receive(ZWaveMessage message) {
        LOG.debug("Received a raw data message: {}", message);

        if(message instanceof ZWaveRawMessage) {
            LOG.debug("Received a raw message from node: {}", ((ZWaveRawMessage) message).getMessageNode());
        }
    }

}
