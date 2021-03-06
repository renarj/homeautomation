package com.oberasoftware.home.state.influxdb;

import com.oberasoftware.home.api.extensions.SpringExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Renze de Vries
 */
@Configuration
@ComponentScan
public class InfluxDBConfiguration implements SpringExtension {
}
