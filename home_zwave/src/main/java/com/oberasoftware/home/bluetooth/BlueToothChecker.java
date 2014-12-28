package com.oberasoftware.home.bluetooth;

import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.bluetooth.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class BlueToothChecker {
    private static final Logger LOG = getLogger(BlueToothChecker.class);

    @PostConstruct
    public void test() {
        LOG.debug("Bean exists");
    }

    @Scheduled(fixedDelay = 10000)
    public void checkBlueToothDevices() {
        LOG.info("Starting bluetooth device discovery");
        try {
            LocalDevice local = LocalDevice.getLocalDevice();

            DiscoveryAgent agent = local.getDiscoveryAgent();
            AtomicBoolean inquiryCompleted = new AtomicBoolean(false);

            boolean inquiryStarted = agent.startInquiry(DiscoveryAgent.GIAC, new DiscoveryListener() {
                public void deviceDiscovered(RemoteDevice device, DeviceClass cod) {
                    try {
                        String name = device.getFriendlyName(false);
                        LOG.debug("Discovered device: {}", name);
                    } catch (IOException e) {
                        LOG.error("", e);
                    }
                }

                @Override
                public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {

                }

                @Override
                public void serviceSearchCompleted(int i, int i1) {

                }

                @Override
                public void inquiryCompleted(int i) {
                    inquiryCompleted.set(true);
                }
            });
            while(!inquiryCompleted.get()) {
                LOG.debug("Waiting for bluetooth inquiry to complete");

                sleepUninterruptibly(1, TimeUnit.SECONDS);
            }
        } catch (BluetoothStateException e) {
            LOG.error("", e);
        }

        LOG.info("Finished bluetooth device discovery");
    }
}
