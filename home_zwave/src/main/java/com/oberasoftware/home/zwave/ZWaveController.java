package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.api.events.EventBus;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.api.Controller;
import com.oberasoftware.home.zwave.api.ZWaveAction;
import com.oberasoftware.home.zwave.api.ZWaveDeviceAction;
import com.oberasoftware.home.zwave.api.events.WaitForWakeUpEvent;
import com.oberasoftware.home.zwave.connector.ControllerConnector;
import com.oberasoftware.home.zwave.core.NodeManager;
import com.oberasoftware.home.zwave.core.NodeStatus;
import com.oberasoftware.home.zwave.core.ZWaveNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @author renarj
 */
@Component
public class ZWaveController implements Controller {
    private static final Logger LOG = LoggerFactory.getLogger(ZWaveController.class);

    @Autowired
    private ControllerConnector connector;

    @Autowired
    private NodeManager nodeManager;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private TransactionManager transactionManager;

    @PreDestroy
    public void disconnect() throws HomeAutomationException {
        if(this.connector != null) {
            LOG.info("Disconnecting ZWave controller connector: {}", this.connector);
            this.connector.close();
        }
    }

    @Override
    public <T> void subscribe(EventListener<T> eventListener) {
        eventBus.addListener(eventListener);
    }

    @Override
    public void send(ZWaveAction message) throws HomeAutomationException {
        if(message instanceof ZWaveDeviceAction) {
            handleDeviceMessage((ZWaveDeviceAction)message);
        } else {
            transactionManager.startAction(message);
        }
    }

    private void handleDeviceMessage(ZWaveDeviceAction deviceAction) throws HomeAutomationException {
        int nodeId = deviceAction.getNodeId();

        ZWaveNode node = nodeManager.getNode(nodeId);
        if(node != null && node.getNodeStatus() != NodeStatus.INITIALIZING) {
            if(node.getNodeInformation().isListening()) {
                LOG.debug("Node: {} is a battery device, waiting for wakeup");
                eventBus.push(new WaitForWakeUpEvent(deviceAction));
            } else {
                //This is most likely an offline node or a battery device
                LOG.debug("Node: {} is listening sending action: {}", deviceAction);
                transactionManager.startAction(deviceAction);
            }
        } else {
            LOG.info("Unknown status of node, firing of event: {} hoping node: {} is online", deviceAction, nodeId);
            transactionManager.startAction(deviceAction);
        }
    }
}
