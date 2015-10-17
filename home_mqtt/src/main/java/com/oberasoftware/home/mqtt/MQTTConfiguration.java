package com.oberasoftware.home.mqtt;

import com.oberasoftware.home.api.extensions.SpringExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Renze de Vries
 */
@Configuration
@ComponentScan
@PropertySource(name = "appProps", value = {"classpath:application.properties"})
public class MQTTConfiguration implements SpringExtension {
}
