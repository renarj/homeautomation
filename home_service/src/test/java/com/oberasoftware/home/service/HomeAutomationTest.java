package com.oberasoftware.home.service;

import com.google.common.util.concurrent.Uninterruptibles;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Renze de Vries
 */
public class HomeAutomationTest {
    @Test
//    @Ignore
    public void testHomeAutomation() {
        new HomeAutomation().start(new String[]{});

        Uninterruptibles.sleepUninterruptibly(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
    }
}
