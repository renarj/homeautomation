package com.oberasoftware.home.zwave;

import com.oberasoftware.home.zwave.experiment.ExperimentConfiguration;
import com.oberasoftware.home.zwave.experiment.SensorService;
import com.oberasoftware.home.zwave.api.actions.controller.ControllerCapabilitiesAction;
import com.oberasoftware.home.zwave.api.actions.controller.ControllerInitialDataAction;
import com.oberasoftware.home.zwave.api.actions.controller.GetControllerIdAction;
import com.oberasoftware.home.zwave.api.actions.devices.BatteryGetAction;
import com.oberasoftware.home.zwave.api.events.EventListener;
import com.oberasoftware.home.zwave.api.events.devices.BinarySensorEvent;
import com.oberasoftware.home.zwave.core.NodeManager;
import com.oberasoftware.home.zwave.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.rules.RuleEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;


@EnableAutoConfiguration
@Import({ZWaveConfiguration.class, ExperimentConfiguration.class})
@Component
public class SpringBootZWaveTest implements EventListener<BinarySensorEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(SpringBootZWaveTest.class);

    @Autowired
    private ZWaveController zWaveController;

    @Autowired
    private NodeManager nodeManager;

    @Autowired
    private RuleEngine ruleEngine;

    @Autowired
    private SensorService sensorService;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        LOG.info("Starting ZWAVE App");

        SpringBootZWaveTest test = SpringApplication.run(SpringBootZWaveTest.class).getBean(SpringBootZWaveTest.class);
        test.initialise();
    }

    /**
     * Initialises the binding. This is called after the 'updated' method
     * has been called and all configuration has been passed.
     */
    public void initialise()  {
        try {
//            zWaveController.subscribe(this);

            LOG.info("Wait for a bit before doing anything");
            sleepUninterruptibly(3, TimeUnit.SECONDS);

//            this.zWaveController.send(new RequestNodeInfoAction(4));

            LOG.info("Wait over, sending initial controller messages");
            zWaveController.send(new ControllerCapabilitiesAction());
            zWaveController.send(new ControllerInitialDataAction());
            zWaveController.send(new GetControllerIdAction());

            while(!zWaveController.isNetworkReady()) {
                LOG.info("Network not ready yet, sleeping");
                sleepUninterruptibly(1, TimeUnit.SECONDS);
            }

            LOG.info("Network is ready, sending initial action");
//            zWaveController.send(new SwitchAction(() -> 4, SwitchAction.STATE.ON));
//            zWaveController.send(new SwitchAction(() -> 7, 100));
//            zWaveController.send(new SwitchAction(() -> 8, 99));
//            zWaveController.send(new SwitchAction(() -> 3, SwitchAction.STATE.OFF));

            LOG.info("Waiting a bit to switch off so we can see some visual effect");
            sleepUninterruptibly(5, TimeUnit.SECONDS);

            LOG.info("Wait over, sending Off message");
//            zWaveController.send(new SwitchAction(() -> 8, SwitchAction.STATE.ON));
//            zWaveController.send(new SwitchAction(() -> 7, SwitchAction.STATE.OFF));

            scheduleNodeChecker();
            scheduleBatteryCheck();

            int movementSensorId = 2;
            int switchDeviceId = 4;

//            ruleEngine.add(when(d -> d.getNodeId() == movementSensorId)
//                    .alsoWhen(DeviceEvent::containsValue)
//                    .alsoWhen(DeviceEvent::isTriggered)
//                    .alsoWhen(() -> sensorService.getCount(movementSensorId), movements -> movements == 2)
//                    .thenDo(new SwitchAction(() -> switchDeviceId, SwitchAction.STATE.ON)));
//
//            ruleEngine.add(when(d -> d.getNodeId() == movementSensorId)
//                    .alsoWhen(DeviceEvent::containsValue)
//                    .alsoWhen(DeviceEvent::isTriggered)
//                    .alsoWhen(() -> sensorService.getCount(movementSensorId), movements -> movements == 3)
//                    .thenDo(new SwitchAction(() -> switchDeviceId, SwitchAction.STATE.OFF)));



            LOG.info("Waiting a bit more, preparing for shutdown in a bit");
            sleepUninterruptibly(3, TimeUnit.SECONDS);


//            zWaveController.disconnect();
        } catch (HomeAutomationException e) {
            LOG.error("", e);
        }
    }

    private void scheduleBatteryCheck() {
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

    private void scheduleNodeChecker() {
        executorService.submit(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                LOG.info("Nodes report");
                nodeManager.getNodes().forEach(n -> LOG.debug("We have a node: {}", n));

                sleepUninterruptibly(30, TimeUnit.SECONDS);
            }
        });
    }

    @Override
    public void receive(BinarySensorEvent message) {
        LOG.debug("Received a binary sensor event from node: {}, sensor is: {}", message.getNodeId(), message.isTriggered() ? "Open" : "Closed");
//        LOG.info("Got an even from node: {} endpoint: {}", message.getNodeId(), message.getEndpointId());
    }
}
