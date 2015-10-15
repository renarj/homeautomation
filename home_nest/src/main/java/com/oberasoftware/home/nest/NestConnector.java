package com.oberasoftware.home.nest;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.oberasoftware.home.api.AutomationBus;
import com.oberasoftware.home.api.events.devices.DeviceValueEventImpl;
import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.core.types.ValueImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class NestConnector {
    private static final Logger LOG = getLogger(NestConnector.class);

    @Value("${nest.token}")
    private String accessToken;

    @Autowired
    private AutomationBus automationBus;

    private AtomicBoolean connected = new AtomicBoolean(false);

    private Firebase firebase;

    public void activate() {
        LOG.info("Connecting to Nest Cloud");

        firebase = new Firebase("https://developer-api.nest.com");
        firebase.auth(accessToken, new Firebase.AuthListener() {
            @Override
            public void onAuthError(FirebaseError firebaseError) {
                LOG.error("Auth Error: {} {} {}", firebaseError.getCode(), firebaseError.getDetails(), firebaseError.getMessage());
            }

            @Override
            public void onAuthSuccess(Object o) {
                LOG.debug("Authentication was successful");
                connected.set(true);

                monitorThermostats();
            }

            @Override
            public void onAuthRevoked(FirebaseError firebaseError) {
                LOG.debug("Authentication was revoked: {} {} {}", firebaseError.getCode(), firebaseError.getDetails(), firebaseError.getMessage());
            }
        });
    }

    private void monitorThermostats() {
        LOG.debug("Registering thermostat monitor");
        firebase.child("devices").child("thermostats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LOG.debug("New data received: {}", dataSnapshot);

                dataSnapshot.getChildren().forEach(c -> {
                    String deviceId = (String) c.child("device_id").getValue();
                    double temp = (Double) c.child("ambient_temperature_c").getValue();
                    Long humidity = (Long) c.child("humidity").getValue();

                    automationBus.publish(new DeviceValueEventImpl(automationBus.getControllerId(), "nest", deviceId, new ValueImpl(VALUE_TYPE.DECIMAL, temp), "temperature"));
                    automationBus.publish(new DeviceValueEventImpl(automationBus.getControllerId(), "nest", deviceId, new ValueImpl(VALUE_TYPE.NUMBER, humidity), "humidity"));
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public boolean isConnected() {
        return connected.get();
    }

    public Firebase getFirebase() {
        return firebase;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
