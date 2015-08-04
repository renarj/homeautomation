package com.oberasoftware.home.api.commands;

import com.oberasoftware.home.api.types.Value;

import java.util.Map;

/**
 * @author renarj
 */
public interface ItemValueCommand extends ItemCommand {

    Value getValue(String property);

    Map<String, Value> getValues();
}
