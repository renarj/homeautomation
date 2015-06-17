package com.oberasoftware.home.nest;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.common.util.concurrent.Uninterruptibles;
import com.oberasoftware.home.api.model.Device;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Renze de Vries
 */
@Component
public class ThermostatManager {
    private static final Logger LOG = getLogger(ThermostatManager.class);

    @Autowired
    private NestConnector nestConnector;

    public List<Device> getDevices() {
        Firebase firebase = nestConnector.getFirebase();
        LOG.debug("Trying to retrieve devices");

        List<Device> thermostats = new ArrayList<>();

        CountDownLatch latch = new CountDownLatch(1);
        firebase.child("devices").child("thermostats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LOG.debug("Received thermostat data: {}", dataSnapshot);
                dataSnapshot.getChildren().forEach(c -> {
                    String deviceId = (String) c.child("device_id").getValue();
                    String name = (String) c.child("name").getValue();
                    LOG.debug("Found device: {} with name: {}", deviceId, name);

                    thermostats.add(new NestThermostat(deviceId, name));
                });
                latch.countDown();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Uninterruptibles.awaitUninterruptibly(latch);

        return thermostats;
    }

    public void setNestConnector(NestConnector nestConnector) {
        this.nestConnector = nestConnector;
    }
}
