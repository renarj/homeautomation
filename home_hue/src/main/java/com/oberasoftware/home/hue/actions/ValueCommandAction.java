package com.oberasoftware.home.hue.actions;

import com.oberasoftware.home.api.commands.DeviceValueCommand;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.hue.HueConnector;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.util.Optional;

import static com.google.common.math.DoubleMath.roundToInt;
import static com.google.common.primitives.Ints.checkedCast;
import static java.util.Optional.of;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class ValueCommandAction implements HueCommandAction<DeviceValueCommand> {
    private static final Logger LOG = getLogger(ValueCommandAction.class);

    @Autowired
    private HueConnector hueConnector;


    @Override
    public void receive(DeviceItem item, DeviceValueCommand command) {
        LOG.debug("Received a value command: {} for item: {}", command, item);

        PHBridge bridge = hueConnector.getSdk().getSelectedBridge();
        Optional<PHLight> light = bridge.getResourceCache().getAllLights().stream()
                .filter(l -> l.getIdentifier().equals(item.getDeviceId()))
                .findFirst();

        LOG.debug("Received a Value command for bulb: {} desired brightness level: {}", item.getDeviceId(), command.getValues());
        if(light.isPresent()) {
            PHLightState st = new PHLightState();
            st.setOn(true);
            Optional<Integer> brightness = getValue(command, "value", true);
            Optional<Integer> saturation = getValue(command, "saturation", true);
            Optional<Integer> color = getValue(command, "color", false);
            Optional<Integer> transitionTime = getValue(command, "transition", false);

            if(brightness.isPresent()) {
                LOG.debug("Setting light brightness: {}", brightness);
                st.setBrightness(brightness.get());
            }
            if(saturation.isPresent()) {
                LOG.debug("Setting light saturation: {}", saturation);
                st.setSaturation(saturation.get());
            }
            if(color.isPresent()) {
                LOG.debug("Setting light color: {}", color);
                st.setHue(color.get());
            }
            if(transitionTime.isPresent()) {
                LOG.debug("Setting light transition time: {}", transitionTime);
                st.setTransitionTime(transitionTime.get());
            }

            bridge.updateLightState(light.get(), st);
        }

    }

    private Optional<Integer> getValue(DeviceValueCommand command, String property, boolean correctScale) {
        Optional<Value> optionalValue = Optional.ofNullable(command.getValue(property));
        if(optionalValue.isPresent()) {
            Value value = optionalValue.get();

            switch(value.getType()) {
                case NUMBER:
                    Long longValue = value.getValue();
                    return of(convert(longValue, correctScale));
                case DECIMAL:
                    Double doubleValue = value.getValue();
                    return of(convert(doubleValue, correctScale));
                case STRING:
                default:
                    LOG.warn("Could not get value, value not a valid Number: {}", value.toString());
            }

        }

        return Optional.empty();
    }

    private Integer convert(Long number, boolean correctScale) {
        int convertedInt = checkedCast(number);

        return correctScale ? scale(convertedInt) : convertedInt;
    }

    private Integer convert(Double decimal, boolean correctScale) {
        int roundedInt = roundToInt(decimal, RoundingMode.CEILING);

        return correctScale ? scale(roundedInt) : roundedInt;
    }

    private int scale(int number) {
        if (number <= 100) {
            int output = (int)(number * ((double)255 / (double)100)); //scale from 0-100 to 0-255 for Hue
            LOG.debug("Converting scale from 0-100 to 0-255 input: {} and output: {}", number, output);
            return output;
        } else if(number <=0) {
            LOG.debug("Input: {} was below minimal scale, assuming minimum brightness", number);
            return 0; //lowest brightness
        } else {
            LOG.debug("Input: {} was beyond scale, assuming maximum brightness", number);
            return 255; //max brightness
        }
    }
}
