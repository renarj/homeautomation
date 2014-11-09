package com.oberasoftware.home.zwave.threading;

import com.oberasoftware.home.api.Topic;
import com.oberasoftware.home.api.EventListener;
import com.oberasoftware.home.api.TopicManager;
import com.oberasoftware.home.zwave.messages.ZWaveRawMessage;
import com.oberasoftware.home.zwave.messages.ByteMessage;
import com.oberasoftware.home.zwave.messages.ZWaveMessage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static com.oberasoftware.home.zwave.ZWAVE_CONSTANTS.CAN;
import static com.oberasoftware.home.zwave.ZWAVE_CONSTANTS.ACK;
import static com.oberasoftware.home.zwave.ZWAVE_CONSTANTS.NAK;

import static com.oberasoftware.home.zwave.ZWAVE_CONSTANTS.ZWAVE_RESPONSE_TIMEOUT;

/**
 * @author Renze de Vries
 */
public class SenderThread extends Thread implements EventListener<ZWaveMessage> {
    private static final Logger LOG = LoggerFactory.getLogger(SenderThread.class);

    private int zWaveResponseTimeout = ZWAVE_RESPONSE_TIMEOUT;

    private final Semaphore barrier = new Semaphore(1);

    private final Topic<ZWaveMessage> senderTopic;

    private final OutputStream outputStream;

    public SenderThread(TopicManager topicManager, OutputStream outputStream) {
        this.outputStream = outputStream;
        senderTopic = topicManager.provideTopic(ZWaveMessage.class, "sender");
    }

//                    // If this message is a data packet to a node
//                    // then make sure the node is not a battery device.
//                    // If it's a battery device, it needs to be awake, or we queue the frame until it is.
//                    if (lastSentMessage.getControllerMessageType() == SerialMessage.SerialMessageClass.SendData) {
//                        ZWaveNode node = getNode(lastSentMessage.getMessageNode());
//
//                        if (node != null && !node.isListening() && !node.isFrequentlyListening() && lastSentMessage.getPriority() != SerialMessage.SerialMessagePriority.Low) {
//                            ZWaveWakeUpCommandClass wakeUpCommandClass = (ZWaveWakeUpCommandClass)node.getCommandClass(ZWaveCommandClass.CommandClass.WAKE_UP);
//
//                            // If it's a battery operated device, check if it's awake or place in wake-up queue.
//                            if (wakeUpCommandClass != null && !wakeUpCommandClass.processOutgoingWakeupMessage(lastSentMessage)) {
//                                continue;
//                            }
//                        }
//                    }



    /**
     * Run method. Runs the actual sending process.
     */
    @Override
    public void run() {
        LOG.debug("Starting Z-Wave send thread");
        while (!isInterrupted()) {
            try {
                LOG.debug("Message queue size: {}", senderTopic.size());

                if(senderTopic.peek().isPresent()) {
                    ZWaveMessage sendMessage = senderTopic.pop();

                    long messageTimeStart = System.currentTimeMillis();
                    if(sendMessage instanceof ZWaveRawMessage) {
                        sendRawMessage((ZWaveRawMessage) sendMessage);

                        // Clear the semaphore used to acknowledge the response.
                        barrier.drainPermits();

                        if (barrier.tryAcquire(1, zWaveResponseTimeout, TimeUnit.MILLISECONDS)) {
                            long responseTime = System.currentTimeMillis() - messageTimeStart;
                            LOG.debug("Response processed after {} ms.", responseTime);
                        } else {
                            LOG.error("NODE {}: Timeout while sending message. Requeueing");
                        }

                    } else if(sendMessage instanceof ByteMessage) {
                        sendByte(((ByteMessage) sendMessage).getSingleByte());
                    }


                } else {
                    LOG.debug("No topics to send, sleeping");
                    sleepUninterruptibly(1, TimeUnit.SECONDS);
                }
            } catch (NoSuchElementException e1) {
                LOG.debug("Sleep no queue elements");
                sleepUninterruptibly(1, TimeUnit.SECONDS);
            } catch (IOException | InterruptedException e) {
                LOG.error("", e);
            }


//            // Now wait for the response...
//            try {
//                if (!barrier.tryAcquire(1, zWaveResponseTimeout, TimeUnit.MILLISECONDS)) {
//                    if (lastSentMessage.getControllerMessageType() == SerialMessage.SerialMessageClass.SendData) {
//
//                        buffer = new SerialMessage(SerialMessage.SerialMessageClass.SendDataAbort, SerialMessage.SerialMessageType.Request, SerialMessage.SerialMessageClass.SendData, SerialMessage.SerialMessagePriority.High).getMessageBuffer();
//                        LOG.debug("Sending Message = " + SerialMessage.bb2hex(buffer));
//                        try {
//                            synchronized (serialPort.getOutputStream()) {
//                                serialPort.getOutputStream().write(buffer);
//                                serialPort.getOutputStream().flush();
//                            }
//                        } catch (IOException e) {
//                            LOG.error("Got I/O exception {} during sending. exiting thread.", e.getLocalizedMessage());
//                            break;
//                        }
//                    }
//
//                    if (--lastSentMessage.attempts >= 0) {
//                        LOG.error("NODE {}: Timeout while sending message. Requeueing", lastSentMessage.getMessageNode());
//                        if (lastSentMessage.getControllerMessageType() == SerialMessage.SerialMessageClass.SendData)
//                            handleFailedSendDataRequest(lastSentMessage);
//                        else
//                            enqueue(lastSentMessage);
//                    } else
//                    {
//                        LOG.warn("NODE {}: Discarding message: {}", lastSentMessage.getMessageNode(), lastSentMessage.toString());
//                    }
//                    continue;
//                }
//            } catch (InterruptedException e) {
//                break;
//            }

        }
        LOG.debug("Stopped Z-Wave send thread");
    }

    private void sendByte(int singleByte) throws IOException {
        LOG.debug("Sending raw byte: {}", singleByte);
        outputStream.write(singleByte);
    }

    private void sendRawMessage(ZWaveRawMessage zWaveRawMessage) {
        byte[] buffer = zWaveRawMessage.getMessageBuffer();
        LOG.debug("Sending Message = " + SerialMessage.bb2hex(buffer));
        try {
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException e) {
            LOG.error("Got I/O exception {} during sending. exiting thread.", e.getLocalizedMessage());
        }
    }

    public void completeTransaction() {
        LOG.debug("Completing send transaction");
        barrier.release();
    }

    @Override
    public void receive(ZWaveMessage message) {
        if(message instanceof ByteMessage) {
            ByteMessage byteMessage = (ByteMessage) message;
            LOG.debug("Received a byte message: {}", byteMessage);

            int receivedByte = byteMessage.getSingleByte();
            switch (receivedByte) {
                case ACK:
                    break;
                case CAN:
                case NAK:
                    LOG.debug("Got a ACK/CAN/NAK response: {}", receivedByte);
                    barrier.release();
            }
        }
    }
}
