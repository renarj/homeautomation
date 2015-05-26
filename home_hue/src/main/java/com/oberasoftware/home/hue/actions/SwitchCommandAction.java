package com.oberasoftware.home.hue.actions;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.commands.SwitchCommand;
import com.oberasoftware.home.api.events.devices.OnOffValueEvent;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.hue.HueConnector;
import com.oberasoftware.home.hue.HueExtension;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
            PHLightState st = new PHLightState();
            st.setOn(switchCommand.getState() == SwitchCommand.STATE.ON);

            bridge.updateLightState(light.get(), st);

            automationBus.publish(new OnOffValueEvent(automationBus.getControllerId(), HueExtension.HUE_ID, item.getDeviceId(), switchCommand.getState() == SwitchCommand.STATE.ON));
        }

    }
}
