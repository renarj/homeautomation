package com.oberasoftware;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@EnableAutoConfiguration
@ComponentScan
public class Monitor {
    private static final Logger LOG = getLogger(Monitor.class);


    public static void main(String[] args) {
        SpringApplication.run(Monitor.class, args);
    }
}
