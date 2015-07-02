package com.oberasoftware.home.arduino;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.events.devices.StateUpdateEvent;
import com.oberasoftware.home.api.managers.DeviceManager;
import com.oberasoftware.home.api.model.State;
import com.oberasoftware.home.api.model.StateItem;
import com.oberasoftware.home.api.model.storage.DeviceItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class ValueListener implements EventHandler {
    private static final Logger LOG = getLogger(ValueListener.class);

    @Autowired
    private DeviceManager deviceManager;

    @Autowired
    private ArduinoLCD lcd;

    @EventSubscribe
    public void receive(StateUpdateEvent stateUpdateEvent) {
        State state = stateUpdateEvent.getState();
        LOG.debug("Received a device state: {}", state);

        DeviceItem d = deviceManager.findDevice(state.getItemId());
        if(d.getPluginId().equals("youless")) {
            long value = state.getStateItem("power").getValue().getValue();
            lcd.write(ArduinoLCD.LINE.FIRST, "Power: " + value + " Watt");
        } else {
            Optional<StateItem> si = state.getStateItems().stream().filter(s -> s.getLabel().equals("temperature")).findAny();
            if(si.isPresent()) {
                double value = si.get().getValue().getValue();
                lcd.write(ArduinoLCD.LINE.SECOND, "Temp: " + value + "c");
            }
        }
    }
}
