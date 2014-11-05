package com.oberasoftware.home;

import com.google.common.util.concurrent.Uninterruptibles;
import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.api.actions.SwitchAction;
import com.oberasoftware.home.zwave.api.events.ZWaveEvent;
import com.oberasoftware.home.zwave.connector.SerialZWaveConnector;
import com.oberasoftware.home.zwave.ZWaveController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class ZWaveTest implements EventListener<ZWaveEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(ZWaveTest.class);

    private ZWaveController zWaveController;

    public static void main(String[] args) {
        LOG.info("Starting ZWAVE App");

        ZWaveTest test = new ZWaveTest();
        test.initialise();
    }

    /**
     * Initialises the binding. This is called after the 'updated' method
     * has been called and all configuration has been passed.
     */
    private void initialise()  {
        try {
            this.zWaveController = new ZWaveController();
            this.zWaveController.connect(new SerialZWaveConnector("/dev/tty.SLAB_USBtoUART"));
//            this.zWaveController.connect(new SerialZWaveConnector("/Users/renarj/ttyAMA0"));
            this.zWaveController.subscribe(this);

            LOG.info("Wait for 20 seconds before putting on light");
            Thread.sleep(3000);


//            this.zWaveController.send(new RequestNodeInfoAction(4));
//            Uninterruptibles.sleepUninterruptibly(20, TimeUnit.SECONDS);

            LOG.info("Wait over, sending message");
            zWaveController.send(new SwitchAction(() -> 4, SwitchAction.STATE.ON));
            Uninterruptibles.sleepUninterruptibly(20, TimeUnit.SECONDS);
//            zWaveController.send(new SwitchAction(() -> 4, SwitchAction.STATE.ON));

            LOG.info("Waiting another 20 seconds, before putting light off");

//            Thread.sleep(3000);
            LOG.info("Wait over, sending Off message");
            zWaveController.send(new SwitchAction(() -> 4, SwitchAction.STATE.OFF));
//            Thread.sleep(5000);
//            zWaveController.send(new SwitchAction(() -> 4, SwitchAction.STATE.OFF));


            LOG.info("Message sent, wait another 20 secs");
            Thread.sleep(5000);


            zWaveController.disconnect();

        } catch (HomeAutomationException | InterruptedException e) {
            LOG.error("", e);
        }
    }

    @Override
    public void receive(ZWaveEvent message) {

//        LOG.info("Got an even from node: {} endpoint: {}", message.getNodeId(), message.getEndpointId());
    }

}
