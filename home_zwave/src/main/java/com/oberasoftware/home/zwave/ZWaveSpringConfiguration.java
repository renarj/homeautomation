package com.oberasoftware.home.zwave;

import com.oberasoftware.home.api.extensions.SpringExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Renze de Vries
 */
@Configuration
@Import(ZWaveConfiguration.class)
@ComponentScan
public class ZWaveSpringConfiguration implements SpringExtension {
}
