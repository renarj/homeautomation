package com.oberasoftware.home.mqtt;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.oberasoftware.base.event.Event;
import com.oberasoftware.home.api.events.devices.DeviceValueEventImpl;
import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.core.types.ValueImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Renze de Vries
 */
@Component("PathPatternFormatter")
public class PathPatternFormatter implements MQTTFormatter {
    private static final Logger LOG = LoggerFactory.getLogger(PathPatternFormatter.class);

    private String pattern;
    private Pattern regexPattern;

    @Override
    public void configure(String pattern) {
        this.pattern = pattern;

        String regexPattern = pattern
                .replace("{controllerId}", "(.*)")
                .replace("{itemId}", "(.*)")
                .replace("{type}", "(.*)");

        LOG.info("Using regex pattern: {}", regexPattern);
        this.regexPattern = Pattern.compile(regexPattern);
    }

    @Override
    public MQTTMessage format(String incomingTopic, String incomingPayload) {
        LOG.debug("Incoming topic: {}", incomingTopic);


        Matcher matcher = regexPattern.matcher(incomingTopic);
        if(matcher.find()) {
            String controllerId = matcher.group(1);
            String itemId = matcher.group(2);
            String type = matcher.group(3);

            if(type.equals("state")) {

                Value value = new ValueImpl(VALUE_TYPE.STRING, incomingPayload);
                Double decimal = Doubles.tryParse(incomingPayload);
                if(decimal != null) {
                    value = new ValueImpl(VALUE_TYPE.DECIMAL, decimal);
                } else {
                    Integer number = Ints.tryParse(incomingPayload);
                    if(number != null) {
                        value = new ValueImpl(VALUE_TYPE.NUMBER, number);
                    }
                }

                Event event = new DeviceValueEventImpl(controllerId, "mqtt", itemId, value, "value");
                return new MQTTMessageImpl(controllerId, itemId, "mqtt", event);
            } else {
                LOG.warn("Received an unsupported MQTT even type: {}", type);
                return null;
            }
        } else {
            LOG.warn("Invalid MQTT message received, topic: {} message: {}", incomingTopic, incomingPayload);
            return null;
        }

    }


}
