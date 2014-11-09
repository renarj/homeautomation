package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.api.ZWaveAction;
import com.oberasoftware.home.zwave.api.events.ControllerEvent;
import com.oberasoftware.home.zwave.connector.ControllerConnector;
import com.oberasoftware.home.zwave.converter.ConverterHandler;
import com.oberasoftware.home.zwave.messages.ZWaveRawMessage;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author renarj
 */
public class TransactionManagerImpl implements TransactionManager {

    private final ControllerConnector connector;

    private AtomicInteger callbackGenerator = new AtomicInteger(1);

    private final ConverterHandler<ZWaveAction, ZWaveRawMessage> actionConverter;

//    private Map<Integer, List<ZWaveAction>>

    public TransactionManagerImpl(ControllerConnector connector) {
        this.connector = connector;
        this.actionConverter = new ConverterHandler<>(v -> v.getClass().getSimpleName());
    }

    @Override
    public void startAction(ZWaveAction action) throws HomeAutomationException {
        ZWaveRawMessage rawMessage = actionConverter.convert(action);
        rawMessage.setCallbackId(getCallbackId());
        rawMessage.setTransmitOptions(0x01 | 0x04 | 0x20);

        connector.send(rawMessage);
    }

    @Override
    public void completeTransaction(ControllerEvent controllerEvent) throws HomeAutomationException {
        connector.completeTransaction();
    }

    private int getCallbackId() {
        int callbackId = callbackGenerator.incrementAndGet();
        if(callbackId > 0xFF) {
            callbackId = callbackGenerator.getAndSet(1);
        }
        return callbackId;
    }
}
