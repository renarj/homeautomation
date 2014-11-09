package com.oberasoftware.home.zwave;

import com.oberasoftware.home.zwave.api.ZWaveAction;
import com.oberasoftware.home.zwave.exceptions.ZWaveException;

/**
 * @author renarj
 */
public interface TransactionManager {
    void startAction(ZWaveAction action) throws ZWaveException;
}
