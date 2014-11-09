package com.oberasoftware.home.zwave.messages;

import com.google.common.collect.Maps;
import com.oberasoftware.home.zwave.messages.ControllerMessageType;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
public class ControllerMessageUtil {
    private static final Logger LOG = getLogger(ControllerMessageUtil.class);

    private static final ControllerMessageUtil INSTANCE = new ControllerMessageUtil();

    private final Map<Integer, ControllerMessageType> messagesMap = new HashMap<>();

    private ControllerMessageUtil() {
        newArrayList(ControllerMessageType.values()).forEach(m -> messagesMap.put(m.getKey(), m));
    }

    /**
     * Lookup function based on the generic device class code.
     * @param i the code to lookup
     * @return enumeration value of the generic device class.
     */
    public static ControllerMessageType getMessageClass(int i) {
        LOG.debug("Getting controller message type for device class: {}", Integer.toHexString(i));
        return INSTANCE.messagesMap.get(i);
    }
}
