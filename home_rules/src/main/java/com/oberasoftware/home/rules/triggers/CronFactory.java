package com.oberasoftware.home.rules.triggers;

import org.quartz.JobDetail;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Renze de Vries
 */
@Component
public class CronFactory {
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // this allows to update triggers in DB when updating settings in config file:
        factory.setOverwriteExistingJobs(true);
//        factory.setDataSource(dataSource);
//        factory.setJobFactory(jobFactory);

        factory.setQuartzProperties(quartzProperties());
//        factory.setTriggers(cronTriggerFactoryBean("", null).getObject());

        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    public CronTriggerFactoryBean cronTriggerFactoryBean(String cronId, String cronExpression, JobDetail jobDetail) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setName(cronId);
        cronTriggerFactoryBean.setJobDetail(jobDetail);
        cronTriggerFactoryBean.setCronExpression(cronExpression);
        cronTriggerFactoryBean.afterPropertiesSet();

        return cronTriggerFactoryBean;
    }


}
