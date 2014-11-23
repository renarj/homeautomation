package com.oberasoftware.home.zwave.threading;

import com.oberasoftware.home.zwave.connector.SerialZWaveConnector;
import com.oberasoftware.home.zwave.messages.ZWaveRawMessage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static com.oberasoftware.home.zwave.ZWAVE_CONSTANTS.ZWAVE_RESPONSE_TIMEOUT;

/**
 * @author Renze de Vries
 */
@Component
public class SenderThread extends Thread {
    private static final Logger LOG = LoggerFactory.getLogger(SenderThread.class);

    private int zWaveResponseTimeout = ZWAVE_RESPONSE_TIMEOUT;

    private final Semaphore barrier = new Semaphore(1);

    private Queue<ZWaveRawMessage> sendQueue = new ConcurrentLinkedQueue<>();

//    private OutputStream outputStream;

    @Autowired
    private SerialZWaveConnector serialZWaveConnector;

    public SenderThread() {

    }

//    public SenderThread(OutputStream outputStream) {
//        this.outputStream = outputStream;
//    }

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
                LOG.debug("Message queue size: {}", sendQueue.size());

                if(sendQueue.peek() != null) {
                    ZWaveRawMessage sendMessage = sendQueue.poll();

                    long messageTimeStart = System.currentTimeMillis();
                    sendRawMessage(sendMessage);

                    // Clear the semaphore used to acknowledge the response.
                    barrier.drainPermits();

                    if (barrier.tryAcquire(1, zWaveResponseTimeout, TimeUnit.MILLISECONDS)) {
                        long responseTime = System.currentTimeMillis() - messageTimeStart;
                        LOG.debug("Response processed after {} ms.", responseTime);
                    } else {
                        LOG.error("NODE {}: Timeout while sending message. Requeueing");
                    }
                } else {
                    LOG.debug("No topics to send, sleeping");
                    sleepUninterruptibly(1, TimeUnit.SECONDS);
                }
            } catch (NoSuchElementException e1) {
                LOG.debug("Sleep no queue elements");
                sleepUninterruptibly(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                LOG.error("Thread was interrupted, stopping");
            }
        }
        LOG.debug("Stopped Z-Wave send thread");
    }

    private void sendRawMessage(ZWaveRawMessage zWaveRawMessage) {
        byte[] buffer = zWaveRawMessage.getMessageBuffer();
        LOG.debug("Sending Message = " + SerialMessage.bb2hex(buffer));
        try {
            OutputStream outputStream = serialZWaveConnector.getOutputStream();
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

    public void queueMessage(ZWaveRawMessage message) {
        sendQueue.add(message);
    }
}
