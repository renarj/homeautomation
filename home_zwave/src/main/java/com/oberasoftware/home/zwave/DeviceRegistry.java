package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.zwave.core.ZWaveNode;

import java.util.List;

/**
 * @author renarj
 */
public interface DeviceRegistry {

    List<Device> getDevices();

    void updateNode(ZWaveNode node);

    String getZwaveId();

    String getZwaveName();
}
