package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.experiment.ExperimentConfiguration;
import com.oberasoftware.home.experiment.TriggerService;
import com.oberasoftware.home.zwave.api.actions.SwitchAction;
import com.oberasoftware.home.zwave.api.actions.controller.ControllerCapabilitiesAction;
import com.oberasoftware.home.zwave.api.actions.controller.ControllerInitialDataAction;
import com.oberasoftware.home.zwave.api.actions.controller.GetControllerIdAction;
import com.oberasoftware.home.zwave.api.events.devices.BinarySensorEvent;
import com.oberasoftware.home.zwave.api.events.devices.DeviceEvent;
import com.oberasoftware.home.zwave.core.NodeManager;
import com.oberasoftware.home.zwave.core.impl.IdentifiedNode;
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
import static com.oberasoftware.home.zwave.rules.ConditionBuilder.when;


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
    private TriggerService triggerService;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

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
            zWaveController.send(new SwitchAction(() -> 4, SwitchAction.STATE.ON));
//            zWaveController.send(new SwitchAction(() -> 7, SwitchAction.STATE.ON));
//            zWaveController.send(new SwitchAction(() -> 3, SwitchAction.STATE.OFF));

            LOG.info("Waiting a bit to switch off so we can see some visual effect");
            sleepUninterruptibly(5, TimeUnit.SECONDS);

            LOG.info("Wait over, sending Off message");
            zWaveController.send(new SwitchAction(() -> 4, SwitchAction.STATE.OFF));
//            zWaveController.send(new SwitchAction(() -> 7, SwitchAction.STATE.OFF));

            scheduleNodeChecker();

            int triggerDeviceId = 2;
            int switchDeviceId = 4;

            ruleEngine.add(when(d -> d.getNodeId() == triggerDeviceId)
                    .alsoWhen(DeviceEvent::containsValue)
                    .alsoWhen(DeviceEvent::isTriggered)
                    .alsoWhen(() -> triggerService.getTriggerCount(triggerDeviceId), triggered -> triggered == 2)
                    .thenDo(new SwitchAction(() -> switchDeviceId, SwitchAction.STATE.ON)));

            ruleEngine.add(when(d -> d.getNodeId() == triggerDeviceId)
                    .alsoWhen(DeviceEvent::containsValue)
                    .alsoWhen(DeviceEvent::isTriggered)
                    .alsoWhen(() -> triggerService.getTriggerCount(triggerDeviceId), triggered -> triggered == 3)
                    .thenDo(new SwitchAction(() -> switchDeviceId, SwitchAction.STATE.OFF)));



            LOG.info("Waiting a bit more, preparing for shutdown in a bit");
            sleepUninterruptibly(3, TimeUnit.SECONDS);


//            zWaveController.disconnect();
        } catch (HomeAutomationException e) {
            LOG.error("", e);
        }
    }

    private void scheduleNodeChecker() {
        executorService.submit(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                LOG.info("Nodes report");
                nodeManager.getNodes().forEach(n -> {
                    if (n instanceof IdentifiedNode) {
                        LOG.debug("We have an identified node: {}", n);
                    } else {
                        LOG.debug("We have a basic node: {}", n.getNodeId());
                    }

                });

                sleepUninterruptibly(3, TimeUnit.SECONDS);
            }
        });
    }

    @Override
    public void receive(BinarySensorEvent message) {
        LOG.debug("Received a binary sensor event from node: {}, sensor is: {}", message.getNodeId(), message.isTriggered() ? "Open" : "Closed");
//        LOG.info("Got an even from node: {} endpoint: {}", message.getNodeId(), message.getEndpointId());
    }
}
