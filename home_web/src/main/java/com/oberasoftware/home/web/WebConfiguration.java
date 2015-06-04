package com.oberasoftware.home.web;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author renarj
 */
@Configuration
@ComponentScan
@EnableWebMvc
public class WebConfiguration extends WebMvcAutoConfiguration {

}
