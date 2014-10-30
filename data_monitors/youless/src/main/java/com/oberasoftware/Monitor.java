package com.oberasoftware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author renarj
 */
@EnableAutoConfiguration
@ComponentScan
public class Monitor {
    public static void main(String[] args) {
        SpringApplication.run(Monitor.class, args);
    }
}
