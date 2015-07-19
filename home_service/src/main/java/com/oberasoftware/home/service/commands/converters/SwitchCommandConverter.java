package com.oberasoftware.home.service.commands.converters;

import com.oberasoftware.home.api.commands.BasicCommand;
import com.oberasoftware.home.api.commands.SwitchCommand;
import com.oberasoftware.home.api.commands.converters.CommandConverter;
import com.oberasoftware.home.api.commands.converters.ConverterType;
import com.oberasoftware.home.core.commands.SwitchCommandImpl;
import org.springframework.stereotype.Component;

/**
 * @author renarj
 */
@Component
public class SwitchCommandConverter implements CommandConverter<BasicCommand, SwitchCommand> {

    @Override
    @ConverterType(commandType = "switch")
    public SwitchCommand map(BasicCommand source) {

        String value = source.getProperties().get("value");
        String itemId = source.getItemId();

        switch(value.toLowerCase()) {
            case "on":
                return new SwitchCommandImpl(itemId, SwitchCommand.STATE.ON);
            case "off":
            default:
                return new SwitchCommandImpl(itemId, SwitchCommand.STATE.OFF);
        }

    }
}
