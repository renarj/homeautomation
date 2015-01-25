package com.oberasoftware.home.nest;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.common.util.concurrent.Uninterruptibles;
import org.slf4j.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
public class SimpleTest {
    private static final Logger LOG = getLogger(SimpleTest.class);


    public static void main(String[] args) {
        String clientId = "26cb0b1f-6dda-403d-8d6e-697596d55591";
        String clientSecret = "B1xvyKNfEwVZxRvV8UvDjx5nI";
        String code = "KMCYH4BS";
        String token = "c.899Hq9G1tFgCSizcgNHJbrCq7fl6F1tMHzjlIBvdVoYHxFxkuJtutcFepvfrTZNOMZA1V5IHWcC9ooRvng0qCH2H82K857oqKxbVLllONJDn8ElgWLmAolKEWVvL4uw2UXd0JYkh3B9WaTEu";

        CountDownLatch latch = new CountDownLatch(1);

        Firebase firebase = new Firebase("https://developer-api.nest.com");
        firebase.auth(token, new Firebase.AuthListener() {
            @Override
            public void onAuthError(FirebaseError firebaseError) {
                LOG.error("Auth Error: {} {} {}", firebaseError.getCode(), firebaseError.getDetails(), firebaseError.getMessage());
            }

            @Override
            public void onAuthSuccess(Object o) {
                LOG.info("Auth was succesfull");
                latch.countDown();
            }

            @Override
            public void onAuthRevoked(FirebaseError firebaseError) {
                LOG.info("Auth was revoked");
            }
        });

        LOG.info("Waiting latch");
        Uninterruptibles.awaitUninterruptibly(latch);

        LOG.info("Latch unblocked, continuing");



        firebase.child("devices").child("thermostats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LOG.info("Data was changed: {} {}", dataSnapshot.getKey(), dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                LOG.info("Data was cancelled");
            }
        });

        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.MINUTES);
        /*

{"access_token":"c.9CyoKEzuWil1aVjxOZclb1wQr6QMhpaXy6cLaQ5zsJXXnhlnXqCKf6KfNrvaHBKPNPT7mRqoDJRepu1WgGusnia9Vs7uzzAh23I87FyoGjM1gGsKNmFKDR8JVsDVWsoOPxFZ6HY492lvECmx","expires_in":315360000}

         */

        /*

curl -v -L https://developer-api.nest.com/devices.json?auth=c.9CyoKEzuWil1aVjxOZclb1wQr6QMhpaXy6cLaQ5zsJXXnhlnXqCKf6KfNrvaHBKPNPT7mRqoDJRepu1WgGusnia9Vs7uzzAh23I87FyoGjM1gGsKNmFKDR8JVsDVWsoOPxFZ6HY492lvECmx -H "Accept: text/event-stream"

         */
    }
}
