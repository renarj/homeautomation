package com.oberasoftware.home.zwave.handlers;

import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.api.events.Subscribe;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.ZWaveController;
import com.oberasoftware.home.zwave.api.ZWaveAction;
import com.oberasoftware.home.zwave.api.actions.controller.IdentifyNodeAction;
import com.oberasoftware.home.zwave.api.events.controller.ControllerInitialDataEvent;
import com.oberasoftware.home.zwave.api.events.controller.NodeInformationEvent;
import com.oberasoftware.home.zwave.core.NodeManager;
import com.oberasoftware.home.zwave.core.utils.EventSupplier;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class NodeEventHandler implements EventListener<ControllerInitialDataEvent> {
    private static final Logger LOG = getLogger(NodeEventHandler.class);

    @Autowired
    private NodeManager nodeManager;

    @Autowired
    private ZWaveController zWaveController;

    @Override
    public void receive(ControllerInitialDataEvent event) throws Exception {
        LOG.debug("Received a initial controller data with node information: {}", event);

        event.getNodeIds().forEach(n -> {
            LOG.debug("Registering node: {}", n);
            nodeManager.registerNode(n);

            send(() -> new IdentifyNodeAction(n));
        });
    }

    @Subscribe
    public void receiveNodeInformation(NodeInformationEvent nodeInformationEvent) {
        LOG.debug("Received node information: {}", nodeInformationEvent);
    }

    private void send(EventSupplier<ZWaveAction> delegate) {
        try {
            zWaveController.send(delegate.get());
        } catch(HomeAutomationException e) {
            LOG.error("Could not request zwaveController event", e);
        }
    }
}
