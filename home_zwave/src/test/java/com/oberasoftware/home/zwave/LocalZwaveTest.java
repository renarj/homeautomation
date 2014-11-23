package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.api.ZWaveSession;
import com.oberasoftware.home.zwave.api.actions.SwitchAction;
import com.oberasoftware.home.zwave.local.LocalZwaveSession;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
public class LocalZwaveTest {
    private static final Logger LOG = getLogger(LocalZwaveTest.class);

    public static void main(String[] args) {
        LOG.info("Starting Local ZWAVE App");

        LocalZwaveTest test = new LocalZwaveTest();
        test.initialise();
    }

    /**
     * Initialises the binding. This is called after the 'updated' method
     * has been called and all configuration has been passed.
     */
    public void initialise() {
        LOG.debug("Application startup");
        try {
            ZWaveSession s = new LocalZwaveSession();

            LOG.info("Wait for a bit before doing anything");
            sleepUninterruptibly(3, TimeUnit.SECONDS);

            LOG.info("Wait over, sending message");
            s.doAction(new SwitchAction(() -> 4, SwitchAction.STATE.ON));
            s.doAction(new SwitchAction(() -> 7, SwitchAction.STATE.ON));

            LOG.info("Waiting a bit to switch off so we can see some visual effect");
            sleepUninterruptibly(1, TimeUnit.SECONDS);

            LOG.info("Wait over, sending Off message");
            s.doAction(new SwitchAction(() -> 4, SwitchAction.STATE.OFF));
            s.doAction(new SwitchAction(() -> 7, SwitchAction.STATE.OFF));

            LOG.info("Light off, preparing for shutdown in a bit");
            sleepUninterruptibly(3, TimeUnit.SECONDS);


//            zWaveController.disconnect();

        } catch (HomeAutomationException e) {
            LOG.error("", e);
        }
    }
}
