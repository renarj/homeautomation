package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.events.controller.DeviceUpdateEvent;
import com.oberasoftware.home.api.model.Device;
import com.oberasoftware.home.zwave.core.NodeManager;
import com.oberasoftware.home.zwave.core.ZWaveNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author renarj
 */
@Component
public class DeviceRegistryImpl implements DeviceRegistry {

    @Autowired
    private NodeManager nodeManager;

    @Autowired
    private AutomationBus automationBus;

    @Override
    public List<Device> getDevices() {
        List<ZWaveNode> nodes = nodeManager.getNodes();

        List<Device> mappedDevices = new ArrayList<>();
        nodes.forEach(n -> mappedDevices.addAll(mapNode(n)));

        return mappedDevices;
    }

    private List<ZWaveDevice> mapNode(ZWaveNode node) {
        List<ZWaveDevice> mappedDevices = new ArrayList<>();
        if(node.getEndpoints().isEmpty()) {
            mappedDevices.add(new ZWaveDevice(node));
        } else {
            node.getEndpoints().forEach(e -> mappedDevices.add(new ZWaveDevice(node, e)));
        }
        return mappedDevices;
    }

    @Override
    public void updateNode(ZWaveNode node) {
        List<ZWaveDevice> devices = mapNode(node);
        devices.forEach(d -> automationBus.publish(new DeviceUpdateEvent(getZwaveId(), d)));
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
