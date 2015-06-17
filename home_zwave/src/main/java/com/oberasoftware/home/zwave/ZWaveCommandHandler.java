package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.commands.Command;
import com.oberasoftware.home.api.commands.DeviceValueCommand;
import com.oberasoftware.home.api.commands.Result;
import com.oberasoftware.home.api.commands.SwitchCommand;
import com.oberasoftware.home.api.extensions.CommandHandler;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.zwave.api.actions.SwitchAction;
import com.oberasoftware.home.zwave.api.events.devices.SwitchEvent;
import com.oberasoftware.home.zwave.api.events.devices.SwitchLevelEvent;
import com.oberasoftware.home.zwave.exceptions.HomeAutomationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.oberasoftware.home.zwave.api.actions.SwitchAction.STATE.OFF;
import static com.oberasoftware.home.zwave.api.actions.SwitchAction.STATE.ON;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class ZWaveCommandHandler implements CommandHandler {
    private static final Logger LOG = getLogger(ZWaveCommandHandler.class);

    @Autowired
    private ZWaveController zWaveController;

    @Autowired
    private AutomationBus automationBus;

    @Override
    public Result receive(DeviceItem item, Command command) {
        LOG.debug("Received a command for ZWave device: {} command: {}", item.getDeviceId(), command);

        if(command instanceof SwitchCommand) {
            SwitchCommand switchCommand = (SwitchCommand) command;

            String[] idParts = item.getDeviceId().split("-");
            int nodeId = Integer.parseInt(idParts[0]);
            int endpointId = Integer.parseInt(idParts[1]);

            SwitchAction.STATE desiredState = switchCommand.getState() == SwitchCommand.STATE.ON ? ON : OFF;

            LOG.debug("Received a switch command for node: {} desired state: {}", nodeId, desiredState);

            try {
                zWaveController.send(new SwitchAction(nodeId, endpointId, desiredState));
            } catch (HomeAutomationException e) {
                LOG.error("", e);
            } finally {
                automationBus.publish(new SwitchEvent(nodeId, endpointId, desiredState == ON));
            }
        } else if(command instanceof DeviceValueCommand) {
            DeviceValueCommand valueCommand = (DeviceValueCommand) command;
            String[] idParts = item.getDeviceId().split("-");
            int nodeId = Integer.parseInt(idParts[0]);
            int endpointId = Integer.parseInt(idParts[1]);

            long level;
            Value value = valueCommand.getValue("value");
            if(value.getType() == VALUE_TYPE.NUMBER) {
                level = value.getValue();
            } else {
                level = Long.parseLong(value.asString());
            }

            try {
                zWaveController.send(new SwitchAction(nodeId, endpointId, (int)level));
            } catch(HomeAutomationException e) {
                LOG.error("", e);
            } finally {
                automationBus.publish(new SwitchLevelEvent(nodeId, (int)level));
            }
        }


        return null;
    }
}
