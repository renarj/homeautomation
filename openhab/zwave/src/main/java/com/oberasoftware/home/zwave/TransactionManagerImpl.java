package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.TopicManager;
import com.oberasoftware.home.zwave.api.ZWaveAction;
import com.oberasoftware.home.zwave.connector.ControllerConnector;
import com.oberasoftware.home.zwave.exceptions.ZWaveException;

import java.util.List;
import java.util.Map;

/**
 * @author renarj
 */
public class TransactionManagerImpl implements TransactionManager {

    private final ControllerConnector connector;

//    private Map<Integer, List<ZWaveAction>>

    public TransactionManagerImpl(ControllerConnector connector, TopicManager topicManager) {
        this.connector = connector;
    }


    @Override
    public void startAction(ZWaveAction action) throws ZWaveException {

    }
}
