package com.oberasoftware.home.bluetooth;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author renarj
 */
@Configuration
@EnableScheduling
@EnableAsync
@ComponentScan
public class BlueToothConfiguration {
}
