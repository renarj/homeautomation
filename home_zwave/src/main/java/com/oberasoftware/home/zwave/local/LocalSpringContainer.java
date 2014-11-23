package com.oberasoftware.home.zwave.local;

import com.oberasoftware.home.zwave.ZWaveConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author renarj
 */
public class LocalSpringContainer {
    private static final ApplicationContext CONTEXT = new AnnotationConfigApplicationContext(ZWaveConfiguration.class);

    static <T> T getBean(Class<T> type) {
        return CONTEXT.getBean(type);
    }
}
