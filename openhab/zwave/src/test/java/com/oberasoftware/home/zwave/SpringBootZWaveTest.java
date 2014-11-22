package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.api.actions.controller.ControllerCapabilitiesAction;
import com.oberasoftware.home.zwave.api.actions.controller.ControllerInitialDataAction;
import com.oberasoftware.home.zwave.api.events.ZWaveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;


@EnableAutoConfiguration
@Import(ZWaveConfiguration.class)
@Component
public class SpringBootZWaveTest implements EventListener<ZWaveEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(SpringBootZWaveTest.class);

    @Autowired
    private ZWaveController zWaveController;

    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        LOG.info("Starting ZWAVE App");

        SpringBootZWaveTest test = SpringApplication.run(SpringBootZWaveTest.class).getBean(SpringBootZWaveTest.class);

//        ZWaveTest test = new ZWaveTest();
        test.initialise();
    }

    /**
     * Initialises the binding. This is called after the 'updated' method
     * has been called and all configuration has been passed.
     */
//    @PostConstruct
    public void initialise()  {
        try {
//            zWaveController.connect(new SerialZWaveConnector("/dev/tty.SLAB_USBtoUART"));
            zWaveController.subscribe(this);

            LOG.info("Wait for a bit before doing anything");
            sleepUninterruptibly(3, TimeUnit.SECONDS);

//            this.zWaveController.send(new RequestNodeInfoAction(4));

            LOG.info("Wait over, sending message");
            zWaveController.send(new ControllerCapabilitiesAction());
            zWaveController.send(new ControllerInitialDataAction());
//            zWaveController.send(new SwitchAction(() -> 4, SwitchAction.STATE.ON));
//            zWaveController.send(new SwitchAction(() -> 7, SwitchAction.STATE.ON));

            LOG.info("Waiting a bit to switch off so we can see some visual effect");
            sleepUninterruptibly(5, TimeUnit.SECONDS);

            LOG.info("Wait over, sending Off message");
//            zWaveController.send(new SwitchAction(() -> 4, SwitchAction.STATE.OFF));
//            zWaveController.send(new SwitchAction(() -> 7, SwitchAction.STATE.OFF));


            LOG.info("Light off, preparing for shutdown in a bit");
            sleepUninterruptibly(3, TimeUnit.SECONDS);

//            zWaveController.disconnect();
        } catch (HomeAutomationException e) {
            LOG.error("", e);
        }
    }

    @Override
    public void receive(ZWaveEvent message) {

//        LOG.info("Got an even from node: {} endpoint: {}", message.getNodeId(), message.getEndpointId());
    }

}
