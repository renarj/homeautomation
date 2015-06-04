package com.oberasoftware.home.zwave;

import com.oberasoftware.home.zwave.api.ZWaveScheduler;
import com.oberasoftware.home.zwave.api.actions.devices.BatteryGetAction;
import com.oberasoftware.home.zwave.core.NodeManager;
import com.oberasoftware.home.zwave.exceptions.HomeAutomationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class ProtocolBootstrap {
    private static final Logger LOG = getLogger(ProtocolBootstrap.class);

    @Autowired
    private ZWaveController zWaveController;

    @Autowired
    private ZWaveScheduler zWaveScheduler;

    @Autowired
    private NodeManager nodeManager;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void startInitialization() {
        LOG.info("Initializing ZWave Network by doing network discovery");
        zWaveController.initializeNetwork();

        scheduleBatteryChecks();
    }


    private void scheduleBatteryChecks() {
        executorService.submit(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                LOG.debug("Retrieving battery information");

                nodeManager.getNodes().stream()
                        .filter(n -> nodeManager.isBatteryDevice(n.getNodeId()))
                        .forEach(n -> {
                            LOG.debug("Getting battery information for node: {}", n.getNodeId());
                            try {
                                zWaveController.send(new BatteryGetAction(n.getNodeId()));
                            } catch (HomeAutomationException e) {
                                LOG.error("", e);
                            }
                        });

                sleepUninterruptibly(30, TimeUnit.MINUTES);
            }
        });

    }
}
