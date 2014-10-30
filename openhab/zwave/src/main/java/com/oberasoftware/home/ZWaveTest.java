package com.oberasoftware.home;

import com.oberasoftware.home.zwave.connector.SerialZWaveConnector;
import com.oberasoftware.home.zwave.ZWaveController;
import com.oberasoftware.home.zwave.messages.ZWaveMessage;
import com.oberasoftware.home.zwave.messages.ZWaveRawMessage;
import com.oberasoftware.home.zwave.exceptions.ZWaveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZWaveTest implements TopicListener<ZWaveMessage> {
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
            this.zWaveController.subscribe(this);

            int setOn = 0xFF;
            int setOff = 0x00;
            int command = setOn;
            final int SWITCH_BINARY_SET = 0x01;
            final int SWITCH_BINARY_GET = 0x02;
            final int SWITCH_BINARY_REPORT = 0x03;

            //SWITCH_BINARY(0x25,"SWITCH_BINARY",ZWaveBinarySwitchCommandClass.class),


            ZWaveRawMessage message = new ZWaveRawMessage(7, ZWaveRawMessage.SerialMessageClass.SendData,
                    ZWaveRawMessage.SerialMessageType.Request, ZWaveRawMessage.SerialMessageClass.SendData, ZWaveRawMessage.SerialMessagePriority.Set);
            byte[] newPayload = { 	(byte) 7,
                    3,
                    (byte) 0x25,
                    (byte) SWITCH_BINARY_SET,
                    (byte) (command > 0 ? 0xFF : 0x00)
            };
            message.setMessagePayload(newPayload);

            LOG.info("Wait for 20 seconds before putting on light");
            Thread.sleep(10000);

            LOG.info("Wait over, sending message");
            zWaveController.send(message);

            LOG.info("Waiting another 20 seconds, before putting light off");
            command = setOff;
            message = new ZWaveRawMessage(7, ZWaveRawMessage.SerialMessageClass.SendData,
                    ZWaveRawMessage.SerialMessageType.Request, ZWaveRawMessage.SerialMessageClass.SendData, ZWaveRawMessage.SerialMessagePriority.Set);
            byte[] offPayload = { 	(byte) 7,
                    3,
                    (byte) 0x25,
                    (byte) SWITCH_BINARY_SET,
                    (byte) (command > 0 ? 0xFF : 0x00)
            };
            message.setMessagePayload(offPayload);

            Thread.sleep(10000);
            LOG.info("Wait over, sending Off message");
            zWaveController.send(message);

            LOG.info("Message sent, wait another 20 secs");
            Thread.sleep(10000);


            zWaveController.disconnect();

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
        } catch (ZWaveException | InterruptedException e) {
            LOG.error("", e);
        }
    }

    @Override
    public void receive(ZWaveMessage message) {

//        LOG.info("Got an even from node: {} endpoint: {}", message.getNodeId(), message.getEndpointId());
    }

//    @Override
//    public void ZWaveIncomingEvent(ZWaveEvent event) {
//        LOG.info("Got an even from node: {} endpoint: {}", event.getNodeId(), event.getEndpoint());
//
//        if(event instanceof ZWaveCommandClassValueEvent) {
//            LOG.info("Raw value: {}", ((ZWaveCommandClassValueEvent) event).getValue());
//        }
//    }
}
