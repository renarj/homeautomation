package com.oberasoftware.home.rest.model;

import com.oberasoftware.home.api.commands.BasicCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * @author renarj
 */
public class BasicRestCommand implements BasicCommand {
    private String itemId;
    private String commandType;

    private Map<String, String> properties = new HashMap<>();

    @Override
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "BasicRESTCommand{" +
                "itemId='" + itemId + '\'' +
                ", commandType='" + commandType + '\'' +
                ", properties=" + properties +
                '}';
    }
}
