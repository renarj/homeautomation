package com.oberasoftware.home.zwave.converter.controller;

import com.google.common.collect.Sets;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.zwave.api.events.controller.NodeInformationEvent;
import com.oberasoftware.home.zwave.converter.ZWaveConverter;
import com.oberasoftware.home.zwave.messages.ZWaveRawMessage;
import com.oberasoftware.home.zwave.messages.types.ControllerMessageType;
import org.slf4j.Logger;

import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
public class IdentifyNodeConverter implements ZWaveConverter<ZWaveRawMessage, NodeInformationEvent> {
    private static final Logger LOG = getLogger(IdentifyNodeConverter.class);

    @Override
    public Set<String> getSupportedTypeNames() {
        return Sets.newHashSet(ControllerMessageType.IdentifyNode.getLabel());
    }

    @Override
    public NodeInformationEvent convert(ZWaveRawMessage source) throws HomeAutomationException {
        LOG.debug("Handling incoming node information event: {}", source);

//        int nodeId = lastSentMessage.getMessagePayloadByte(0);
//        LOG.debug("NODE {}: ProtocolInfo", nodeId);
//
//        ZWaveNode node = zController.getNode(nodeId);
//
//        boolean listening = (incomingMessage.getMessagePayloadByte(0) & 0x80)!=0 ? true : false;
//        boolean routing = (incomingMessage.getMessagePayloadByte(0) & 0x40)!=0 ? true : false;
//        int version = (incomingMessage.getMessagePayloadByte(0) & 0x07) + 1;
//        boolean frequentlyListening = (incomingMessage.getMessagePayloadByte(1) & 0x60)!= 0 ? true : false;
//
//        LOG.debug("NODE {}: Listening = {}", nodeId, listening);
//        LOG.debug("NODE {}: Routing = {}", nodeId, routing);
//        LOG.debug("NODE {}: Version = {}", nodeId, version);
//        LOG.debug("NODE {}: fLIRS = {}", nodeId, frequentlyListening);
//
//        node.setListening(listening);
//        node.setRouting(routing);
//        node.setVersion(version);
//        node.setFrequentlyListening(frequentlyListening);
//
//        ZWaveDeviceClass.Basic basic = ZWaveDeviceClass.Basic.getBasic(incomingMessage.getMessagePayloadByte(3));
//        if (basic == null) {
//            LOG.error(String.format("NODE %d: Basic device class 0x%02x not found", nodeId, incomingMessage.getMessagePayloadByte(3)));
//            return false;
//        }
//        LOG.debug(String.format("NODE %d: Basic = %s 0x%02x", nodeId, basic.getLabel(), basic.getKey()));
//
//        ZWaveDeviceClass.Generic generic = ZWaveDeviceClass.Generic.getGeneric(incomingMessage.getMessagePayloadByte(4));
//        if (generic == null) {
//            LOG.error(String.format("NODE %d: Generic device class 0x%02x not found", nodeId, incomingMessage.getMessagePayloadByte(4)));
//            return false;
//        }
//        LOG.debug(String.format("NODE %d: Generic = %s 0x%02x", nodeId, generic.getLabel(), generic.getKey()));
//
//        ZWaveDeviceClass.Specific specific = ZWaveDeviceClass.Specific.getSpecific(generic, incomingMessage.getMessagePayloadByte(5));
//        if (specific == null) {
//            LOG.error(String.format("NODE %d: Specific device class 0x%02x not found", nodeId, incomingMessage.getMessagePayloadByte(5)));
//            return false;
//        }
//        LOG.debug(String.format("NODE %d: Specific = %s 0x%02x", nodeId, specific.getLabel(), specific.getKey()));
//
//        ZWaveDeviceClass deviceClass = node.getDeviceClass();
//        deviceClass.setBasicDeviceClass(basic);
//        deviceClass.setGenericDeviceClass(generic);
//        deviceClass.setSpecificDeviceClass(specific);
//
//        // advance node stage of the current node.
//        node.advanceNodeStage(NodeStage.PING);

        return new NodeInformationEvent();
    }
}
