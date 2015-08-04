package com.oberasoftware.home.hue.actions;

import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.commands.ItemValueCommand;
import com.oberasoftware.home.api.events.devices.DeviceValueEventImpl;
import com.oberasoftware.home.api.events.items.ItemNumericValue;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import com.oberasoftware.home.api.model.storage.GroupItem;
import com.oberasoftware.home.api.model.storage.HomeEntity;
import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.core.types.ValueImpl;
import com.oberasoftware.home.hue.HueConnector;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHGroup;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.google.common.math.DoubleMath.roundToInt;
import static com.google.common.primitives.Ints.checkedCast;
import static java.util.Optional.of;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class ValueCommandAction implements HueCommandAction<ItemValueCommand> {
    private static final Logger LOG = getLogger(ValueCommandAction.class);

    @Autowired
    private HueConnector hueConnector;

    @Autowired
    private AutomationBus automationBus;

    @Override
    public void receive(GroupItem groupItem, List<DeviceItem> items, ItemValueCommand command) {
        PHBridge bridge = hueConnector.getSdk().getSelectedBridge();

        PHGroup group = GroupHelper.getOrCreateGroup(groupItem, hueConnector.getBridge(), items);
        LOG.debug("Received a Value group event: {} setting light state: {}", group, command);

        List<String> groupLightIds = group.getLightIdentifiers();
        List<String> deviceItemIds = items.stream().map(HomeEntity::getId).collect(Collectors.toList());

        if (!groupLightIds.isEmpty()) {
            PHBridgeResourcesCache resource = bridge.getResourceCache();
            Map<String, PHLight> lights = resource.getLights();

            String firstLight = groupLightIds.get(0);
            PHLight light = lights.get(firstLight);
            LOG.debug("Determined first light in group for determining capabilities: {}", light);

            bridge.setLightStateForGroup(group.getIdentifier(),
                    getTargetState(v -> deviceItemIds
                                    .forEach(i -> automationBus.publish(new ItemNumericValue(i, v.value, v.label))),
                    light, command));
        } else {
            LOG.warn("Detected a Philips Hue group without lights");
        }
    }

    @Override
    public void receive(DeviceItem item, ItemValueCommand command) {
        LOG.debug("Received a value command: {} for item: {}", command, item);

        PHBridge bridge = hueConnector.getSdk().getSelectedBridge();
        Optional<PHLight> light = bridge.getResourceCache().getAllLights().stream()
                .filter(l -> l.getIdentifier().equals(item.getDeviceId()))
                .findFirst();

        LOG.debug("Received a Value command for bulb: {} desired brightness level: {}", item.getDeviceId(), command.getValues());
        if(light.isPresent()) {
            bridge.updateLightState(light.get(),
                    getTargetState(v -> automationBus.publish(new DeviceValueEventImpl(item.getControllerId(),
                            item.getPluginId(), item.getDeviceId(), v.value, v.label)), light.get(), command));
        }
    }

    private class LocalValue {
        private final Value value;
        private final String label;

        public LocalValue(Value value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    private PHLightState getTargetState(Consumer<LocalValue> valueAction, PHLight light, ItemValueCommand command) {
        PHLightState st = new PHLightState();
        st.setOn(true);
        Optional<Integer> brightness = getNumberValue(command, "value", true);
        Optional<Integer> saturation = getNumberValue(command, "saturation", true);
        Optional<String> rgbValue = getValue(command, "rgb");
        Optional<Integer> transitionTime = getNumberValue(command, "transition", false);

        if(brightness.isPresent()) {
            LOG.debug("Setting light brightness: {}", brightness);
            st.setBrightness(brightness.get());

            valueAction.accept(new LocalValue(new ValueImpl(VALUE_TYPE.NUMBER, getNumberValue(command, "value", false).get()), "value"));
        }
        if(saturation.isPresent()) {
            LOG.debug("Setting light saturation: {}", saturation);
            st.setSaturation(saturation.get());

            valueAction.accept(new LocalValue(new ValueImpl(VALUE_TYPE.NUMBER, saturation.get()), "saturation"));
        }
        if(rgbValue.isPresent()) {
            LOG.debug("Setting light color: {}", rgbValue.get());

            setLighXY(light, st, rgbValue.get());

            valueAction.accept(new LocalValue(new ValueImpl(VALUE_TYPE.STRING, rgbValue.get()), "rgb"));
        }
        if(transitionTime.isPresent()) {
            LOG.debug("Setting light transition time: {}", transitionTime);
            st.setTransitionTime(transitionTime.get());
        }

        return st;
    }

    private void setLighXY(PHLight light, PHLightState state, String hexRgbValue) {
        int red = Integer.valueOf(hexRgbValue.substring( 1, 3 ), 16);
        int green = Integer.valueOf(hexRgbValue.substring( 3, 5 ), 16);
        int blue = Integer.valueOf(hexRgbValue.substring( 5, 6 ), 16);
        float[] xy = PHUtilities.calculateXYFromRGB(red, green, blue, light.getModelNumber());

        state.setX(xy[0]);
        state.setY(xy[1]);
    }

    private Optional<String> getValue(ItemValueCommand command, String property) {
        Optional<Value> optionalValue = Optional.ofNullable(command.getValue(property));
        if(optionalValue.isPresent()) {
            return Optional.of(optionalValue.get().asString());
        } else {
            return Optional.empty();
        }
    }

    private Optional<Integer> getNumberValue(ItemValueCommand command, String property, boolean correctScale) {
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
