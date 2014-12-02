package com.oberasoftware.home.zwave.handlers;

import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.api.events.Subscribe;
import com.oberasoftware.home.zwave.TransactionManager;
import com.oberasoftware.home.zwave.api.events.SendDataEvent;
import com.oberasoftware.home.zwave.api.events.WaitForWakeUpEvent;
import com.oberasoftware.home.zwave.api.events.devices.WakeUpNoMoreInformationEvent;
import com.oberasoftware.home.zwave.api.events.devices.WakeUpReceivedEvent;
import com.oberasoftware.home.zwave.core.NodeManager;
import com.oberasoftware.home.zwave.core.NodeStatus;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class WakeUpHandler implements EventListener<WakeUpReceivedEvent> {
    private static final Logger LOG = getLogger(WakeUpHandler.class);

    private final ConcurrentMap<Integer, Queue<WaitForWakeUpEvent>> waitForWakeUpEvents = new ConcurrentHashMap<>();

    @Autowired
    private TransactionManager transactionManager;

    @Autowired
    private NodeManager nodeManager;

    @Override
    public void receive(WakeUpReceivedEvent event) throws Exception {
        LOG.debug("Received a wake-up event for node: {}", event.getNodeId());

        nodeManager.setNodeStatus(event.getNodeId(), NodeStatus.AWAKE);

        queueDeviceAction(event.getNodeId());
    }

    @Subscribe
    public void dateReceived(SendDataEvent sendDataEvent) throws Exception {
        LOG.debug("Received a node confirmation from node: {}", sendDataEvent.getNodeId());
        queueDeviceAction(sendDataEvent.getNodeId());
    }

    private void queueDeviceAction(int nodeId) throws Exception {
        Queue<WaitForWakeUpEvent> queue = waitForWakeUpEvents.getOrDefault(nodeId, new LinkedBlockingQueue<>());
        LOG.debug("Sending device actions to node: {} actions waiting: {}", nodeId, queue.size());

        WaitForWakeUpEvent deviceEvent = queue.poll();
        if(deviceEvent != null) {
            transactionManager.startAction(deviceEvent.getDeviceAction());
        }
    }

    @Subscribe
    public void wakeUpNoMoreInformation(WakeUpNoMoreInformationEvent noMoreInformationEvent) {
        LOG.debug("Node: {} has gone back to sleep", noMoreInformationEvent.getNodeId());
        nodeManager.setNodeStatus(noMoreInformationEvent.getNodeId(), NodeStatus.SLEEPING);
    }

    @Subscribe
    public void waitForWakeUp(WaitForWakeUpEvent waitForWakeUpEvent) {
        LOG.debug("Received an action: {} that needs to wait for device wake-up", waitForWakeUpEvent);

        waitForWakeUpEvents.putIfAbsent(waitForWakeUpEvent.getNodeId(), new ConcurrentLinkedQueue<>());
        waitForWakeUpEvents.get(waitForWakeUpEvent.getNodeId()).add(waitForWakeUpEvent);
    }
}
