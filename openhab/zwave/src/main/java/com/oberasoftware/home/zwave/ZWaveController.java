package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.Topic;
import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.api.TopicManager;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.core.TopicManagerImpl;
import com.oberasoftware.home.zwave.api.Controller;
import com.oberasoftware.home.zwave.api.ZWaveAction;
import com.oberasoftware.home.zwave.api.events.ControllerEvent;
import com.oberasoftware.home.zwave.api.events.ZWaveEvent;
import com.oberasoftware.home.zwave.connector.ControllerConnector;
import com.oberasoftware.home.zwave.converter.ConverterHandler;
import com.oberasoftware.home.zwave.exceptions.ZWaveException;
import com.oberasoftware.home.zwave.messages.ZWaveMessage;
import com.oberasoftware.home.zwave.messages.ZWaveRawMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author renarj
 */
public class ZWaveController implements Controller, EventListener<ZWaveMessage> {
    private static final Logger LOG = LoggerFactory.getLogger(ZWaveController.class);

    private ControllerConnector connector;

    private final ConverterHandler<ZWaveAction, ZWaveRawMessage> actionConverter;

    private final ConverterHandler<ZWaveRawMessage, ControllerEvent> controllerEventConverter;

    private final Topic<ControllerEvent> controllerEventTopic;
    private final Topic<ZWaveEvent> deviceEvents;

    public ZWaveController() {
        TopicManager topicManager = new TopicManagerImpl();
        actionConverter = new ConverterHandler<>(v -> v.getClass().getSimpleName());
        controllerEventConverter = new ConverterHandler<>(v -> v.getControllerMessageType().getLabel());

        controllerEventTopic = topicManager.provideTopic(ControllerEvent.class);
        deviceEvents = topicManager.provideTopic(ZWaveEvent.class);
    }

    @Override
    public void connect(ControllerConnector connector) throws HomeAutomationException {
        this.connector = connector;

        LOG.info("Connecting to controller with connector: {}", connector);
        connector.connect();
        connector.subscribe(this);
    }

    @Override
    public void disconnect() throws HomeAutomationException {
        if(this.connector != null) {
            LOG.info("Disconnecting ZWave controller connector: {}", this.connector);
            this.connector.close();
        }
    }

    @Override
    public Topic<ZWaveEvent> getEventQueue() throws ZWaveException {
        return deviceEvents;
    }

    @Override
    public void subscribe(EventListener<ZWaveEvent> eventListener) {
        deviceEvents.subscribe(eventListener);
    }

    @Override
    public void send(ZWaveAction message) throws HomeAutomationException {
        ZWaveRawMessage rawMessage = actionConverter.convert(message);

        connector.send(rawMessage);
    }

    @Override
    public void receive(ZWaveMessage message) {
        LOG.debug("Received a raw data message: {}", message);

        if(message instanceof ZWaveRawMessage) {
            LOG.debug("Received a raw message from node: {}", ((ZWaveRawMessage) message).getNodeId());

            try {
                ZWaveEvent event = controllerEventConverter.convert((ZWaveRawMessage) message);
                LOG.debug("Event received: {}", event);
            } catch (HomeAutomationException e) {
                LOG.error("", e);
            }
//            int messageSize = ((ZWaveRawMessage) message).getMessagePayload().length;
//            LOG.debug("Message payload size: {}", messageSize);
//            if(messageSize > 1) {
//                LOG.debug("Node could also be: {}", ((ZWaveRawMessage) message).getMessagePayloadByte(1));
//                LOG.debug("Command class: {}", ((ZWaveRawMessage) message).getControllerMessageType());
//                LOG.debug("Callback Id: {}", ((ZWaveRawMessage) message).getMessagePayloadByte(0));
//
//                if(messageSize > 2) {
//                    int commandClassCode = ((ZWaveRawMessage) message).getMessagePayloadByte(3);
//                    ZWaveCommandClass.CommandClass commandClass = ZWaveCommandClass.CommandClass.getCommandClass(commandClassCode);
//                    LOG.debug("Command class: {} code: {}", commandClass, commandClassCode);
//                }
//            }
        }
    }

}
