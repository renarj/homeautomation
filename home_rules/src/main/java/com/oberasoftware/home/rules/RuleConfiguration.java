package com.oberasoftware.home.rules;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Renze de Vries
 */
@Configuration
@ComponentScan
@EnableScheduling
public class RuleConfiguration {


//
//    private static JobDetailFactoryBean createJobDetail(Class jobClass) {
//        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
//        factoryBean.setJobClass(jobClass);
//        // job has to be durable to be stored in DB:
//        factoryBean.setDurability(true);
//        return factoryBean;
//    }
//
//    private static SimpleTriggerFactoryBean createTrigger(JobDetail jobDetail, long pollFrequencyMs) {
//        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
//        factoryBean.setJobDetail(jobDetail);
//        factoryBean.setStartDelay(0L);
//        factoryBean.setRepeatInterval(pollFrequencyMs);
//        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
//        // in case of misfire, ignore all missed triggers and continue :
//        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
//        return factoryBean;
//    }
}
