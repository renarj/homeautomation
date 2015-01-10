package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.commands.Command;
import com.oberasoftware.home.api.commands.Result;
import com.oberasoftware.home.api.commands.SwitchCommand;
import com.oberasoftware.home.api.extensions.CommandHandler;
import com.oberasoftware.home.api.storage.model.DeviceItem;
import com.oberasoftware.home.zwave.api.actions.SwitchAction;
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

    @Override
    public Result receive(DeviceItem item, Command command) {
        LOG.debug("Received a command for ZWave device: {} command: {}", item.getDeviceId(), command);

        if(command instanceof SwitchCommand) {
            SwitchCommand switchCommand = (SwitchCommand) command;

            int nodeId = Integer.parseInt(item.getDeviceId());
            SwitchAction.STATE desiredState = switchCommand.getState() == SwitchCommand.STATE.ON ? ON : OFF;

            LOG.debug("Received a switch command for node: {} desired state: {}", nodeId, desiredState);

            try {
                zWaveController.send(new SwitchAction(() -> nodeId, desiredState));
            } catch (HomeAutomationException e) {
                LOG.error("", e);
            }
        }


        return null;
    }
}
