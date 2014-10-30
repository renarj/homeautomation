package org.openhab.binding.zwave.internal;

import org.openhab.binding.zwave.internal.config.ZWaveConfiguration;
import org.openhab.binding.zwave.internal.protocol.SerialInterfaceException;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEventListener;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveBinarySwitchCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZWaveTest implements ZWaveEventListener {
    private static final Logger LOG = LoggerFactory.getLogger(ZWaveTest.class);

    private ZWaveController zWaveController;
    private ZWaveNetworkMonitor networkMonitor;

    public static void main(String[] args) {
        LOG.info("Starting ZWAVE App");

        ZWaveTest test = new ZWaveTest();
        test.initialise();;


    }

    /**
     * Initialises the binding. This is called after the 'updated' method
     * has been called and all configuration has been passed.
     */
    private void initialise()  {
        try {
            this.zWaveController = new ZWaveController(false, "/dev/tty.SLAB_USBtoUART", null);
            zWaveController.initialize();
            zWaveController.addEventListener(this);

            // The network monitor service needs to know the controller...
            this.networkMonitor = new ZWaveNetworkMonitor(this.zWaveController);


            int setOn = 0xFF;
            int setOff = 0x00;
            int command = setOn;
            final int SWITCH_BINARY_SET = 0x01;
            final int SWITCH_BINARY_GET = 0x02;
            final int SWITCH_BINARY_REPORT = 0x03;

            //SWITCH_BINARY(0x25,"SWITCH_BINARY",ZWaveBinarySwitchCommandClass.class),


            SerialMessage message = new SerialMessage(7, SerialMessage.SerialMessageClass.SendData,
                    SerialMessage.SerialMessageType.Request, SerialMessage.SerialMessageClass.SendData, SerialMessage.SerialMessagePriority.Set);
            byte[] newPayload = { 	(byte) 7,
                    3,
                    (byte) 0x25,
                    (byte) SWITCH_BINARY_SET,
                    (byte) (command > 0 ? 0xFF : 0x00)
            };
            message.setMessagePayload(newPayload);

            LOG.info("Wait for 20 seconds before putting on light");
            Thread.sleep(20000);

            LOG.info("Wait over, sending message");
            zWaveController.enqueue(message);

            LOG.info("Waiting another 20 seconds, before putting light off");
            command = setOff;
            message = new SerialMessage(7, SerialMessage.SerialMessageClass.SendData,
                    SerialMessage.SerialMessageType.Request, SerialMessage.SerialMessageClass.SendData, SerialMessage.SerialMessagePriority.Set);
            byte[] offPayload = { 	(byte) 7,
                    3,
                    (byte) 0x25,
                    (byte) SWITCH_BINARY_SET,
                    (byte) (command > 0 ? 0xFF : 0x00)
            };
            message.setMessagePayload(offPayload);

            Thread.sleep(20000);
            LOG.info("Wait over, sending Off message");
            zWaveController.enqueue(message);

            LOG.info("Message sent, wait another 20 secs");
            Thread.sleep(20000);




//            if(healtime != null) {
//                this.networkMonitor.setHealTime(healtime);
//            }
//            if(softReset != false) {
//                this.networkMonitor.resetOnError(softReset);
//            }

            // The config service needs to know the controller and the network monitor...
//            this.zConfigurationService = new ZWaveConfiguration(this.zController, this.networkMonitor);
//            zController.addEventListener(this.zConfigurationService);
//            return;
        } catch (SerialInterfaceException | InterruptedException e) {
            LOG.error("", e);
        }
    }

    @Override
    public void ZWaveIncomingEvent(ZWaveEvent event) {
        LOG.info("Got an even from node: {} endpoint: {}", event.getNodeId(), event.getEndpoint());

        if(event instanceof ZWaveCommandClassValueEvent) {
            LOG.info("Raw value: {}", ((ZWaveCommandClassValueEvent) event).getValue());
        }
    }
}
