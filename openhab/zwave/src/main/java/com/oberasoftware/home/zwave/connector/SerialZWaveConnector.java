package com.oberasoftware.home.zwave.connector;

import com.oberasoftware.home.api.Topic;
import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.api.TopicManager;
import com.oberasoftware.home.core.TopicManagerImpl;
import com.oberasoftware.home.zwave.messages.ByteMessage;
import com.oberasoftware.home.zwave.threading.ReceiverThread;
import com.oberasoftware.home.zwave.threading.SenderThread;
import com.oberasoftware.home.zwave.messages.ZWaveRawMessage;
import com.oberasoftware.home.zwave.exceptions.ZWaveException;
import com.oberasoftware.home.zwave.messages.ZWaveMessage;
import gnu.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.google.common.util.concurrent.Uninterruptibles.joinUninterruptibly;
import static com.oberasoftware.home.zwave.ZWAVE_CONSTANTS.ZWAVE_RECEIVE_TIMEOUT;
import static com.oberasoftware.home.zwave.ZWAVE_CONSTANTS.RECEIVER_TOPIC;
import static com.oberasoftware.home.zwave.ZWAVE_CONSTANTS.SENDER_TOPIC;

/**
 * @author renarj
 */
public class SerialZWaveConnector implements ControllerConnector {
    private static final Logger LOG = LoggerFactory.getLogger(SerialZWaveConnector.class);

    private final String portName;

    private SerialPort serialPort;

    private SenderThread senderThread;
    private ReceiverThread receiverThread;

    private final TopicManager topicManager;

    private final Topic<ZWaveMessage> receiverTopic;
    private final Topic<ZWaveMessage> senderTopic;

    public SerialZWaveConnector(String portName) {
        this.portName = portName;
        this.topicManager = new TopicManagerImpl();
        this.receiverTopic = topicManager.provideTopic(ZWaveMessage.class, RECEIVER_TOPIC);
        this.senderTopic = topicManager.provideTopic(ZWaveMessage.class, SENDER_TOPIC);
    }

    /**
     * Connect to the zwave controller
     *
     * @throws ZWaveException if unable to connect to the serial device
     */
    @Override
    public void connect() throws ZWaveException {
        LOG.info("Connecting to ZWave serial port device: {}", portName);
        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            CommPort commPort = portIdentifier.open("org.openhab.binding.zwave",2000);
            this.serialPort = (SerialPort) commPort;
            this.serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
            this.serialPort.enableReceiveThreshold(1);
            this.serialPort.enableReceiveTimeout(ZWAVE_RECEIVE_TIMEOUT);


            this.receiverThread = new ReceiverThread(topicManager, serialPort.getInputStream(), serialPort.getOutputStream());
            this.receiverThread.start();

            this.senderThread = new SenderThread(topicManager, serialPort.getOutputStream());
            this.senderThread.start();

            this.receiverTopic.subscribe(ByteMessage.class.getSimpleName(), senderThread);

            LOG.info("ZWave controller is connected");
        } catch (NoSuchPortException e) {
            throw new ZWaveException(String.format("Serial port %s does not exist", portName), e);
        } catch (PortInUseException e) {
            throw new ZWaveException(String.format("Serial port %s is in use", portName), e);
        } catch (UnsupportedCommOperationException e) {
            throw new ZWaveException(String.format("Unsupported operation on serial port %s", portName), e);
        } catch (IOException e) {
            throw new ZWaveException(String.format("Unable to open outputstream to serial port %s", portName), e);
        }
    }

    @Override
    public void close() throws ZWaveException {
        senderThread.interrupt();
        receiverThread.interrupt();

        joinUninterruptibly(senderThread);
        joinUninterruptibly(receiverThread);

        serialPort.close();
        serialPort = null;
    }

    @Override
    public void send(ZWaveRawMessage rawMessage) {
        this.senderTopic.push(rawMessage);
    }

    @Override
    public void subscribe(EventListener<ZWaveMessage> zWaveMessageTopicListener) {
        receiverTopic.subscribe(zWaveMessageTopicListener);
    }

    @Override
    public boolean isConnected() {
        return serialPort != null;
    }

    @Override
    public String toString() {
        return "SerialZWaveConnector{" +
                "portName='" + portName + '\'' +
                ", serialPort=" + serialPort +
                ", topicManager=" + topicManager +
                ", receiverTopic=" + receiverTopic +
                ", senderTopic=" + senderTopic +
                '}';
    }
}
