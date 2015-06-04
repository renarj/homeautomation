package com.oberasoftware.home.service.commands;

import com.oberasoftware.home.api.commands.DeviceValueCommand;
import com.oberasoftware.home.api.types.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * @author renarj
 */
public class ValueCommandImpl implements DeviceValueCommand {

    private final String itemId;

    private Map<String, Value> values = new HashMap<>();

    public ValueCommandImpl(String itemId, Map<String, Value> values) {
        this.itemId = itemId;
        this.values = values;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public Value getValue(String property) {
        return values.get(property);
    }

    @Override
    public Map<String, Value> getValues() {
        return values;
    }
}
