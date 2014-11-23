package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.api.events.EventBus;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.api.Controller;
import com.oberasoftware.home.zwave.api.ZWaveAction;
import com.oberasoftware.home.zwave.api.events.ZWaveEvent;
import com.oberasoftware.home.zwave.connector.ControllerConnector;
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
    public void subscribe(EventListener<ZWaveEvent> eventListener) {
//        deviceEvents.subscribe(eventListener);
        eventBus.addListener(eventListener);
    }

    @Override
    public void send(ZWaveAction message) throws HomeAutomationException {
        transactionManager.startAction(message);
    }
}
