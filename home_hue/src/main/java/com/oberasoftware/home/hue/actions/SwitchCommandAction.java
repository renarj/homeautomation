package com.oberasoftware.home.hue.actions;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.commands.SwitchCommand;
import com.oberasoftware.home.api.events.OnOffValue;
import com.oberasoftware.home.api.events.devices.DeviceValueEventImpl;
import com.oberasoftware.home.api.events.items.ItemNumericValue;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.model.storage.GroupItem;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.hue.HueConnector;
import com.oberasoftware.home.hue.HueExtension;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHGroup;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class SwitchCommandAction implements HueCommandAction<SwitchCommand> {
    private static final Logger LOG = getLogger(SwitchCommandAction.class);


    @Autowired
    private HueConnector hueConnector;

    @Autowired
    private AutomationBus automationBus;

    @Override
    public void receive(DeviceItem item, SwitchCommand switchCommand) {
        PHBridge bridge = hueConnector.getSdk().getSelectedBridge();
        Optional<PHLight> light = bridge.getResourceCache().getAllLights().stream()
                .filter(l -> l.getIdentifier().equals(item.getDeviceId()))
                .findFirst();

        LOG.debug("Received a switch command for bulb: {} desired state: {}", item.getDeviceId(), switchCommand.getState());
        if(light.isPresent()) {
            bridge.updateLightState(light.get(), getTargetState(switchCommand));

            OnOffValue value = new OnOffValue(switchCommand.getState() == SwitchCommand.STATE.ON);

            automationBus.publish(new DeviceValueEventImpl(automationBus.getControllerId(), HueExtension.HUE_ID, item.getDeviceId(),value, OnOffValue.LABEL));
        }
    }

    @Override
    public void receive(GroupItem groupItem, List<DeviceItem> items, SwitchCommand command) {
        PHBridge bridge = hueConnector.getSdk().getSelectedBridge();

        PHGroup group = GroupHelper.getOrCreateGroup(groupItem, hueConnector.getBridge(), items);
        LOG.debug("Received a Switch group event: {} setting light state: {}", group, command.getState());

        bridge.setLightStateForGroup(group.getIdentifier(), getTargetState(command));

        Value value = new OnOffValue(command.getState() == SwitchCommand.STATE.ON);
        items.forEach(i -> automationBus.publish(new ItemNumericValue(i.getId(), value, OnOffValue.LABEL)));
    }

    private PHLightState getTargetState(SwitchCommand switchCommand) {
        PHLightState st = new PHLightState();
        st.setOn(switchCommand.getState() == SwitchCommand.STATE.ON);

        return st;
    }
}
