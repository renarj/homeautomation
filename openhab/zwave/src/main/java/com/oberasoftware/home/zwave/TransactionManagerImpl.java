package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.api.ZWaveAction;
import com.oberasoftware.home.zwave.api.events.ControllerEvent;
import com.oberasoftware.home.zwave.connector.ControllerConnector;
import com.oberasoftware.home.zwave.converter.ConverterHandler;
import com.oberasoftware.home.zwave.messages.ZWaveRawMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author renarj
 */
@Component
public class TransactionManagerImpl implements TransactionManager {

    @Autowired
    private ControllerConnector connector;

    @Autowired
    private ConverterHandler<ZWaveAction, ZWaveRawMessage> converterHandler;

    private AtomicInteger callbackGenerator = new AtomicInteger(1);

    @Override
    public void startAction(ZWaveAction action) throws HomeAutomationException {
        ZWaveRawMessage rawMessage = converterHandler.convert(v -> v.getClass().getSimpleName(), action);
        rawMessage.setCallbackId(getCallbackId());
        rawMessage.setTransmitOptions(0x01 | 0x04 | 0x20);

        connector.send(rawMessage);
    }

    @Override
    public void completeTransaction(ControllerEvent controllerEvent) throws HomeAutomationException {
        connector.completeTransaction();
    }

    @Override
    public void cancelTransaction() throws HomeAutomationException {
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
