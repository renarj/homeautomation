package com.oberasoftware.home.zwave.converter.controller;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author renarj
 */
public class ControllerMessageUtil {

    private static final Map<Integer, ControllerMessageType> messagesMap = new HashMap<>();
    {
        newArrayList(ControllerMessageType.values()).forEach(m -> messagesMap.put(m.getKey(), m));
    }

    /**
     * Lookup function based on the generic device class code.
     * @param i the code to lookup
     * @return enumeration value of the generic device class.
     */
    public static ControllerMessageType getMessageClass(int i) {
        return messagesMap.get(i);
    }
}
