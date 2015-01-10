package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.zwave.core.NodeManager;
import com.oberasoftware.home.zwave.core.ZWaveNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author renarj
 */
@Component
public class DeviceRegistryImpl implements DeviceRegistry {

    @Autowired
    private NodeManager nodeManager;

    @Override
    public List<Device> getDevices() {
        List<ZWaveNode> nodes = nodeManager.getNodes();

        return nodes.stream().map(ZWaveDevice::new).collect(Collectors.toList());
    }

    @Override
    public String getZwaveId() {
        return "zwave";
    }

    @Override
    public String getZwaveName() {
        return "ZWave provider";
    }
}
